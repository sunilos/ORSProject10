# UserCtl API Trace

`UserCtl extends BaseReportCtl<UserForm, UserDTO, UserServiceInt>`, so it inherits list /
search / add / update / delete for free. This document covers only the six endpoints
`UserCtl` declares itself — account self-service and password recovery, none of which fit
the generic CRUD shape. Each trace follows the real code path, including a couple of
behaviors worth flagging as you read them.

## Contents

- [`GET /user/preload`](#get-userpreload--role-list-for-the-user-form)
- [`POST /user/myprofile`](#post-usermyprofile--update-the-logged-in-user)
- [`POST /user/changepassword`](#post-userchangepassword)
- [`POST /user/forgetPassword`](#post-userforgetpassword)
- [`PUT /user/profilePhoto/{userId}`](#put-userprofilephotouserid)
- [`GET /user/forgotPassword/{loginId}`](#get-userforgotpasswordloginid)
- [Endpoint map](#endpoint-map)

---

## GET /user/preload — role list for the user form

```mermaid
sequenceDiagram
    actor C as Client
    participant Ctl as UserCtl
    participant RSvc as RoleServiceInt

    C->>Ctl: GET /user/preload
    activate Ctl
    Ctl->>RSvc: roleService.preloadList(new RoleDTO(), ctx)
    activate RSvc
    RSvc-->>Ctl: List<key/value maps>
    deactivate RSvc
    Ctl->>Ctl: wrap roleList in ORSResponse
    Ctl-->>C: 200 OK { data: roleList }
    deactivate Ctl
```

**Collaborators:** `UserCtl` (the only user-specific class involved) and `RoleServiceInt`
(supplies dropdown options).

> **Note.** Mirrors `FacultyCtl.preload()`: `UserForm`, `UserDTO`, `UserServiceImpl`, and
> `UserDAOImpl` never run. This exists purely to fill the role dropdown on the add/edit user
> screen.

---

## POST /user/myprofile — update the logged-in user

```mermaid
sequenceDiagram
    actor C as Client
    participant Ctl as UserCtl
    participant Frm as MyProfileForm
    participant Svc as UserServiceImpl
    participant Dao as UserDAOImpl
    participant DB as RT_USER (DB)

    C->>Ctl: POST /user/myprofile (body: MyProfileForm)
    activate Ctl
    Note over Ctl,Frm: @Valid runs bean validation on MyProfileForm<br/>MyProfileForm has no getDto() - fields are copied by hand
    Ctl->>Ctl: valiate(bindingResult)
    alt validation failed
        Ctl-->>C: res.success = false (field errors)
    end
    Ctl->>Svc: baseService.findById(userContext.getUserId(), ctx)
    activate Svc
    Svc->>Dao: baseDao.findByPK(userId, ctx)
    activate Dao
    Dao->>DB: entityManager.find(UserDTO.class, userId)
    DB-->>Dao: UserDTO
    Dao-->>Svc: UserDTO
    deactivate Dao
    Svc-->>Ctl: UserDTO (logged-in user's row)
    deactivate Svc
    Ctl->>Ctl: copy firstName/lastName/dob/phone/gender<br/>from Frm onto UserDTO
    Ctl->>Svc: baseService.update(dto, ctx)
    activate Svc
    Svc->>Dao: baseDao.update(dto, ctx)
    activate Dao
    Dao->>Dao: checkDuplicate(dto, ctx) - loginId/email uniqueKeys
    Dao->>Dao: set modifiedBy / modifiedDatetime
    Dao->>Dao: populate(dto, ctx) - resolve roleName if roleId set
    Dao->>DB: entityManager.merge(dto)
    deactivate Dao
    Svc-->>Ctl: (void)
    deactivate Svc
    Ctl-->>C: 200 OK (res.success = true)
    deactivate Ctl
```

**Collaborators:** `UserCtl` (fetches, hand-copies fields), `MyProfileForm` (plain bean, no
`getDto()`), `UserServiceImpl` (`findById` + `update`, both inherited), `UserDAOImpl`.

> **Note.** `MyProfileForm` doesn't extend `BaseForm` and has no `getDto()`. Unlike the
> Faculty write paths, the controller loads the existing row first and mutates it
> field-by-field — `loginId` is deliberately left untouched (the assignment line is commented
> out in source).

---

## POST /user/changepassword

```mermaid
sequenceDiagram
    actor C as Client
    participant Ctl as UserCtl
    participant Frm as ChangePasswordForm
    participant Svc as UserServiceImpl
    participant Dao as UserDAOImpl
    participant DB as RT_USER (DB)
    participant Mail as EmailServiceImpl

    C->>Ctl: POST /user/changepassword (body: ChangePasswordForm)
    activate Ctl
    Ctl->>Ctl: valiate(bindingResult)
    alt validation failed
        Ctl-->>C: res.success = false
    end
    Ctl->>Ctl: read loginId from userContext.getUserDTO()
    Ctl->>Svc: baseService.changePassword(loginId, oldPassword, newPassword, ctx)
    activate Svc
    Svc->>Dao: baseDao.findByUniqueKey("loginId", loginId, null)
    activate Dao
    Dao->>DB: JPA criteria query
    DB-->>Dao: UserDTO
    Dao-->>Svc: UserDTO
    deactivate Dao
    Svc->>Svc: oldPassword.equals(dto.getPassword())?
    alt old password mismatch
        Svc-->>Ctl: null
        Ctl-->>C: res.success = false, "Invalid old password"
    end
    Svc->>Svc: dto.setPassword(newPassword)
    Svc->>Svc: update(dto, ctx) [inherited - checkDuplicate, populate, merge]
    Svc->>Mail: emailService.send(EmailDTO "U-CP", null)
    activate Mail
    Mail-->>Svc: sent
    deactivate Mail
    Svc-->>Ctl: UserDTO (password changed)
    deactivate Svc
    Ctl-->>C: 200 OK, "Password has been changed"
    deactivate Ctl
```

**Collaborators:** `UserCtl` (validates, reads `loginId` from session context),
`UserServiceImpl` (custom `changePassword()`, not inherited), `UserDAOImpl`
(`findByUniqueKey`, then `update`), `EmailServiceImpl` ("U-CP" confirmation email).

---

## POST /user/forgetPassword

```mermaid
sequenceDiagram
    actor C as Client
    participant Ctl as UserCtl
    participant Frm as ForgetPasswordForm
    participant Svc as UserServiceImpl
    participant Dao as UserDAOImpl
    participant DB as RT_USER (DB)
    participant Mail as EmailServiceImpl

    C->>Ctl: POST /user/forgetPassword (body: ForgetPasswordForm)
    activate Ctl
    Ctl->>Ctl: valiate(bindingResult)
    Note right of Ctl: validation result is computed<br/>but never checked before continuing
    Ctl->>Svc: baseService.forgotPassword(form.getLogin())
    activate Svc
    Svc->>Dao: baseDao.findByUniqueKey("loginId", login, null)
    activate Dao
    Dao->>DB: JPA criteria query
    DB-->>Dao: UserDTO or null
    Dao-->>Svc: UserDTO
    deactivate Dao
    alt not found
        Svc-->>Ctl: null
        Ctl-->>C: res.success = false, "LoginId / Email not found."
    end
    Svc->>Mail: emailService.send(EmailDTO "U-FP", password in params)
    activate Mail
    Mail-->>Svc: sent
    deactivate Mail
    Svc-->>Ctl: UserDTO
    deactivate Svc
    Ctl->>Mail: emailSender.send(EmailDTO "101", null)
    activate Mail
    Mail-->>Ctl: sent
    deactivate Mail
    Note over Ctl,Mail: a second, differently-coded email goes out here -<br/>the service already sent one for "U-FP"
    Ctl-->>C: 200 OK, "Hello {name}! Your password has been sent..."
    deactivate Ctl
```

**Collaborators:** `UserCtl` (also autowires its own `EmailServiceImpl`), `UserServiceImpl`
(custom `forgotPassword()`), `UserDAOImpl` (`findByUniqueKey` lookup only),
`EmailServiceImpl` (sent from *two* places).

> **Flag.** Two things worth a second look here. `valiate(bindingResult)` is called but its
> result is never checked, so a form that fails `@Email @NotEmpty` still proceeds. And the
> plaintext password is emailed twice: once by `UserServiceImpl.forgotPassword()` (message
> code `U-FP`), then again by the controller itself with message code `101`.

---

## PUT /user/profilePhoto/{userId}

```mermaid
sequenceDiagram
    actor C as Client
    participant Ctl as UserCtl
    participant Svc as UserServiceImpl
    participant Dao as UserDAOImpl
    participant DB as RT_USER (DB)
    participant DocCtl as DocumentCtl

    C->>Ctl: PUT /user/profilePhoto/{userId} (multipart file)
    activate Ctl
    alt file missing or empty
        Ctl-->>C: res.success = false, "No file provided"
    end
    Ctl->>Svc: baseService.findById(userId, ctx)
    activate Svc
    Svc->>Dao: baseDao.findByPK(userId, ctx)
    Dao->>DB: entityManager.find(UserDTO.class, userId)
    DB-->>Dao: UserDTO or null
    Dao-->>Svc: UserDTO
    Svc-->>Ctl: UserDTO
    deactivate Svc
    alt user not found
        Ctl-->>C: res.success = false, "User not found"
    end
    Ctl->>Ctl: oldImageId = userDTO.getImageId()
    Ctl->>DocCtl: documentCtl.addFile(file, description, ctx)
    activate DocCtl
    Note right of DocCtl: direct bean call, not HTTP -<br/>ctx passed explicitly since @ModelAttribute doesn't run
    DocCtl-->>Ctl: ORSResponse with DocumentDTO
    deactivate DocCtl
    Ctl->>Ctl: userDTO.setImageId(uploadedDocumentDTO.getId())
    Ctl->>Svc: baseService.save(userDTO, ctx)
    activate Svc
    Svc->>Dao: baseDao.update(dto, ctx) [id greater than 0]
    Dao->>Dao: checkDuplicate, set modifiedBy/modifiedDatetime, populate
    Dao->>DB: entityManager.merge(dto)
    deactivate Svc
    alt oldImageId greater than 0
        Ctl->>DocCtl: documentCtl.deleteDocument(oldImageId, ctx)
        activate DocCtl
        DocCtl-->>Ctl: best-effort, exceptions logged and swallowed
        deactivate DocCtl
    end
    Ctl-->>C: 200 OK, "Profile photo uploaded successfully"
    deactivate Ctl
```

**Collaborators:** `UserCtl` (orchestrates upload, swap, cleanup), `UserServiceImpl`
(`findById` + `save`, both inherited), `UserDAOImpl`, `DocumentCtl` (called as a Java bean,
not over HTTP).

> **Note.** Structurally identical to `FacultyCtl.uploadFacultyProfilePhoto()`, same
> template: fetch entity, delegate the file to `DocumentCtl`, point the entity's `imageId` at
> the new document, save, then best-effort delete the old document.

---

## GET /user/forgotPassword/{loginId}

```mermaid
sequenceDiagram
    actor C as Client
    participant Ctl as UserCtl
    participant Svc as UserServiceImpl
    participant Dao as UserDAOImpl
    participant DB as RT_USER (DB)
    participant Mail as EmailServiceImpl

    C->>Ctl: GET /user/forgotPassword/{loginId}
    activate Ctl
    Ctl->>Svc: baseService.forgotPassword(loginId)
    activate Svc
    Svc->>Dao: baseDao.findByUniqueKey("loginId", loginId, null)
    Dao->>DB: JPA criteria query
    DB-->>Dao: UserDTO or null
    Dao-->>Svc: UserDTO
    alt found
        Svc->>Mail: emailService.send(EmailDTO "U-FP", password in params)
        activate Mail
        Mail-->>Svc: sent
        deactivate Mail
    end
    Svc-->>Ctl: UserDTO or null
    deactivate Svc
    alt not found
        Ctl-->>C: res.success = false, "Login id is not exist"
    else found
        Ctl-->>C: 200 OK { data: UserDTO }
    end
    deactivate Ctl
```

**Collaborators:** `UserCtl` (`@GetMapping("forgotPassword/{loginId}")`), `UserServiceImpl`
(same `forgotPassword()` as the `POST` endpoint above), `UserDAOImpl`, `EmailServiceImpl`.

> **Flag.** Two more things worth flagging. Spelling drifts between the two reset
> endpoints — `POST /user/forgetPassword` vs. `GET /user/forgotPassword/{loginId}` — easy to
> mistype when integrating against this API. And on success the response body is the full
> `UserDTO`, which carries the `password` field back to the client. The Java method is also
> literally named `myProfile` here, an overload of the unrelated method behind
> `POST /user/myprofile`.

---

## Endpoint map

| Endpoint | Collaborators beyond UserCtl |
|---|---|
| `GET /preload` | `RoleServiceInt` only. No `UserForm` / `UserDTO` / `UserServiceImpl` / `UserDAOImpl`. |
| `POST /myprofile` | `MyProfileForm` (no `getDto()`) → `UserServiceImpl.findById/update` → `UserDAOImpl`. |
| `POST /changepassword` | `ChangePasswordForm` → `UserServiceImpl.changePassword()` (custom) → `UserDAOImpl` → `EmailServiceImpl`. |
| `POST /forgetPassword` | `ForgetPasswordForm` → `UserServiceImpl.forgotPassword()` (custom) → `UserDAOImpl` → `EmailServiceImpl` × 2 (service + controller). |
| `PUT /profilePhoto/{id}` | No form. `UserServiceImpl.findById/save` → `UserDAOImpl`, plus `DocumentCtl` called directly as a bean. |
| `GET /forgotPassword/{loginId}` | No form. Same `UserServiceImpl.forgotPassword()` as the `POST` endpoint, reused from a `GET`. |

---

*Classes referenced: `com.sunilos.ctl.UserCtl` · `com.sunilos.form.{MyProfileForm,
ChangePasswordForm, ForgetPasswordForm}` · `com.sunilos.dto.UserDTO` ·
`com.sunilos.service.UserServiceImpl` · `com.sunilos.dao.UserDAOImpl` ·
`com.sunilos.service.RoleServiceInt` · `com.sunilos.ctl.DocumentCtl` ·
`com.sunilos.common.mail.EmailServiceImpl`*
