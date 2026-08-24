# RxJS in this project

This document explains where and how [RxJS](https://rxjs.dev) (`~7.8.0`) is used in the ORS UI codebase, with real examples pulled from the project.

Angular's `HttpClient` returns RxJS `Observable`s under the hood, so RxJS is present throughout the app implicitly. This doc focuses on the places where RxJS operators and types are used **explicitly**.

## Where it's used

| File | RxJS imports | Purpose |
|---|---|---|
| [`src/app/interceptors/auth.interceptor.ts`](src/app/interceptors/auth.interceptor.ts) | `catchError`, `switchMap`, `throwError` | Catch `401` responses and transparently refresh the auth token |
| [`src/app/services/auth.service.ts`](src/app/services/auth.service.ts) | `Observable` | Type the return values of HTTP calls |
| [`src/app/navbar/navbar.ts`](src/app/navbar/navbar.ts) | `Subscription`, `filter` | Listen to router events and manage the subscription lifecycle |

Beyond these, `Observable`/`.subscribe()` (supplied by `@angular/common/http`, backed by RxJS) also show up in [`http-client-service.ts`](src/app/services/http-client-service.ts) and [`auth-image.directive.ts`](src/app/directives/auth-image.directive.ts), described below.

---

## 1. HTTP interceptor — `catchError` + `switchMap` for token refresh

**File:** [`src/app/interceptors/auth.interceptor.ts`](src/app/interceptors/auth.interceptor.ts)

This is the most RxJS-heavy piece of the app. It's a functional `HttpInterceptorFn` that:

1. Attaches the auth token to every outgoing request.
2. Uses `catchError` to intercept `401 Unauthorized` responses.
3. Uses `switchMap` to swap the failed request's error stream for a *new* observable — calling `refreshToken()`, saving the new access token, then retrying the original request with the new token.
4. Uses `throwError` to re-raise errors that shouldn't (or couldn't) be recovered from — either the original error, or a failure from the refresh call itself.

```typescript
import { catchError, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.getToken();
  const authReq = token ? addToken(req, token) : req;

  return next(authReq).pipe(
    catchError((err) => {
      if (err.status === 401 && authService.getRefreshToken()) {
        // Swap the errored stream for the refresh-token stream
        return authService.refreshToken().pipe(
          switchMap((res) => {
            authService.saveToken(res.access);
            // Retry the original request with the new token
            return next(addToken(req, res.access));
          }),
          catchError((refreshErr) => {
            authService.logout();
            router.navigate(['/login']);
            return throwError(() => refreshErr);
          })
        );
      }
      return throwError(() => err);
    })
  );
};
```

**Why these operators:**
- `catchError` — lets the interceptor recover from an error by returning a *new* observable instead of propagating the failure.
- `switchMap` — flattens the `refreshToken()` observable into the interceptor's output stream, so the caller sees a single request/response chain (refresh → retry) rather than nested observables.
- `throwError(() => err)` — the factory form (`() => err`) is required in modern RxJS so a fresh error is created per subscription instead of a single shared error instance.

---

## 2. Typing HTTP responses with `Observable<T>`

**File:** [`src/app/services/auth.service.ts`](src/app/services/auth.service.ts)

Angular's `HttpClient` methods (`get`, `post`, etc.) return `Observable<T>` by design. This service imports `Observable` from `rxjs` purely as a **type** for its method signatures:

```typescript
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  login(credentials: LoginCredentials): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.loginUrl, credentials);
  }

  refreshToken(): Observable<{ access: string }> {
    const refresh = localStorage.getItem('refresh_token') ?? '';
    return this.http.post<{ access: string }>(this.refreshUrl, { refresh });
  }
}
```

This is what makes `authService.refreshToken().pipe(...)` possible in the interceptor above — the caller gets a typed, pipeable stream instead of `any`.

> Note: other methods on this same service (`signin`, `register`, `forgotPassword`, `changePassword`) skip the typed `Observable` return and instead call `.subscribe({ next, error })` internally, exposing plain callback parameters instead. This is an inconsistency in the current code — `login`/`refreshToken` are the more idiomatic RxJS usage in this file.

---

## 3. Filtering router events with `filter`, cleaning up with `Subscription`

**File:** [`src/app/navbar/navbar.ts`](src/app/navbar/navbar.ts)

The navbar needs to know whenever navigation finishes, so it can decide whether to show/hide itself on the login page. `Router.events` emits *every* router lifecycle event (`NavigationStart`, `NavigationEnd`, `NavigationCancel`, ...), so `filter` narrows the stream down to just `NavigationEnd`:

```typescript
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

export class NavbarComponent implements OnInit, OnDestroy {
  private routerSub!: Subscription;

  ngOnInit(): void {
    this.checkRoute(this.router.url);
    this.routerSub = this.router.events
      .pipe(filter(e => e instanceof NavigationEnd))
      .subscribe((e: any) => this.checkRoute(e.urlAfterRedirects));
  }

  ngOnDestroy(): void {
    this.routerSub?.unsubscribe();
  }
}
```

**Why this matters:** `Router.events` is a long-lived, app-scoped observable — it never completes on its own. Any component that subscribes to it manually (instead of using the `async` pipe) **must** unsubscribe in `ngOnDestroy`, or the subscription — and the component it closes over — leaks for the lifetime of the app. This is the only place in the codebase that manages a `Subscription` by hand.

---

## 4. Implicit RxJS via `HttpClient.subscribe()`

Most of the app doesn't touch RxJS operators directly — it just calls `.subscribe({ next, error })` on the `Observable` returned by `HttpClient`, using plain callback parameters instead of composing operators. Two representative examples:

**[`src/app/services/http-client-service.ts`](src/app/services/http-client-service.ts)** — a thin wrapper around `HttpClient` used by most list/detail components:

```typescript
get<T>(url: string, onSuccess: (data: T) => void, onError: (error: unknown) => void, params?: Record<string, string>): void {
  const httpParams = params ? new HttpParams({ fromObject: params }) : undefined;
  this.http.get<T>(url, { params: httpParams }).subscribe({ next: onSuccess, error: onError });
}
```

**[`src/app/directives/auth-image.directive.ts`](src/app/directives/auth-image.directive.ts)** — fetches an image as a `Blob` through the (interceptor-authenticated) `HttpClient` and turns it into an object URL:

```typescript
ngOnChanges(): void {
  this.revoke();
  if (!this.src) return;

  this.http.get(this.src, { responseType: 'blob' }).subscribe({
    next: (blob) => {
      this.objectUrl = URL.createObjectURL(blob);
      this.el.nativeElement.src = this.objectUrl;
    },
    error: () => { }
  });
}

ngOnDestroy(): void {
  this.revoke(); // revokes the created object URL
}
```

These don't use `pipe()` or operators, but every `Observable` here is still RxJS — `HttpClient` is built on it.

---

## Summary

| Concept | Used? | Where |
|---|---|---|
| `Observable` as a type | ✅ | `auth.service.ts` |
| `pipe()` + operators (`catchError`, `switchMap`, `filter`) | ✅ | `auth.interceptor.ts`, `navbar.ts` |
| Manual `Subscription` + `unsubscribe()` | ✅ | `navbar.ts` |
| `async` pipe (template-driven subscription) | ❌ | not used anywhere |
| `Subject` / `BehaviorSubject` (cross-component state streams) | ❌ | not used anywhere |
| NgRx / NgRx Signals (store built on RxJS) | ❌ | not used — see project notes |

The project relies on RxJS mainly at the HTTP boundary (interceptor + `HttpClient`) and for one router-event stream, rather than as an app-wide reactive state layer.
