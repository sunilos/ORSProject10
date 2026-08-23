import { Directive, ElementRef, Input, OnChanges, OnDestroy, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

/**
 * Fetches `appAuthSrc` via HttpClient (so the auth interceptor attaches the
 * token) and assigns the resulting blob to the host `<img>` as an object URL.
 * Use this instead of a plain `[src]` binding for images served by
 * authenticated endpoints.
 */
@Directive({
  selector: 'img[appAuthSrc]',
  standalone: true
})
export class AuthImageDirective implements OnChanges, OnDestroy {

  @Input('appAuthSrc') src: string | null = null;

  private readonly http = inject(HttpClient);
  private readonly el = inject(ElementRef<HTMLImageElement>);
  private objectUrl: string | null = null;

  ngOnChanges(): void {
    this.revoke();
    this.el.nativeElement.src = '';
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
    this.revoke();
  }

  private revoke(): void {
    if (this.objectUrl) {
      URL.revokeObjectURL(this.objectUrl);
      this.objectUrl = null;
    }
  }
}
