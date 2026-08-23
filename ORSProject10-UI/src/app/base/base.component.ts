import { ChangeDetectorRef, Directive, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BaseService } from '../services/base.service';

/**
 * Abstract base class for all CRUD form components (Role, College, Course, Student, Subject, Faculty).
 *
 * Provides the complete add / edit / delete lifecycle out of the box:
 *   - `ngOnInit` calls `loadDropdowns()` then `loadEntity()`
 *   - `loadEntity()` reads router state or falls back to a `getById` API call
 *   - `onSave()` dispatches to `add` or `update` via `getService()`
 *   - `onDelete()` calls `delete` via `getService()`
 *
 * Child classes must implement the six abstract members and may override
 * `loadDropdowns()` to pre-load any FK dropdown data before the form opens.
 */
@Directive()
export abstract class BaseComponent implements OnInit {

  /** True when the component is opened for an existing record (URL contains `:id`). */
  isEditMode = false;

  /** True while the entity is being fetched from the API on initial load. */
  loading = false;

  /** True while a save (add or update) request is in flight. */
  saving = false;

  /** True while a delete request is in flight. */
  deleting = false;

  /** Holds the error message shown in the form when an API call fails. */
  errorMessage = '';

  errors: Record<string, string> = {};

  /** Holds the raw response from the preload API; available to child components. */
  preloadData: any;

  /** The numeric ID parsed from the route parameter, or `null` in add-mode. */
  entityId: number | null = null;

  /** Route path of the list page — used by `goBack()` and after save/delete. */
  protected abstract listUrl: string;

  /** Page heading shown in the template; returns 'Add …' or 'Edit …'. */
  abstract get title(): string;

  /**
   * The reactive form for this entity; used by `onSave()` to validate and read values.
   * Assigned in `ngOnInit()` via `this.form = this.buildForm()`.
   */
  form!: FormGroup;

  /**
   * Constructs and returns the `FormGroup` for this entity.
   * Called from `ngOnInit()`. Define all controls and validators here.
   */
  protected abstract buildForm(): FormGroup;

  /**
   * Returns the service instance responsible for CRUD operations on this entity.
   * Called by `onSave()`, `onDelete()`, and `loadEntity()`.
   */
  protected abstract getService(): BaseService;

  /**
   * Builds and returns the request payload sent to the API on save.
   * Typically `{ id: this.entityId ?? 0, ...this.form.value }`.
   */
  protected abstract getBody(): unknown;

  /**
   * Patches the reactive form with values from `data`.
   * Called with either `history.state` (row-click navigation) or the API response from `getById`.
   * @param data - The entity object to populate the form with.
   */
  protected abstract populateForm(data: any): void;

  /**
   * It's used later via this.cdr.markForCheck() — inside async callbacks 
   * (HTTP success/error handlers). markForCheck() 
   * tells Angular "this component's data changed, 
   * re-run change detection on it
   * 
   * inject is alternate way injectinging a service without using constructor
   */
  protected readonly cdr = inject(ChangeDetectorRef);

  /** Shared `FormBuilder` used by child components' `buildForm()`. */
  protected readonly fb = inject(FormBuilder);

  //add comment
  protected readonly router = inject(Router);

  // add comment  
  protected readonly route = inject(ActivatedRoute);

  /**
   * Angular lifecycle hook. Builds the form, then runs `loadDropdowns()` so FK
   * selects are populated before the form values are patched, then `loadEntity()`.
   */
  ngOnInit(): void {
    console.log('BaseComponent.ngOnInit() called');
    this.form = this.buildForm();
    this.loadDropdowns();
    this.loadEntity();
  }

  /**
   * Override in child components that need to pre-load dropdown data
   * (e.g. college list for Student, course list for Subject).
   * Called before `loadEntity()` so the selects are ready when the form is patched.
   */
  protected loadDropdowns(): void {
    console.log('BaseComponent.loadDropdowns() called');
    this.getService().preload((r: any) => {
      this.preloadData = r.result?.data ?? [];
      console.log('preloadData', this.preloadData);
      this.cdr.markForCheck();
    }, () => { });
  }

  /**
   * Initialises edit-mode and populates the form.
   * - In add-mode (`/entity/new`): `initEditMode()` returns false and the method exits early.
   * - In edit-mode (`/entity/:id`): reads `history.state` first (set by the list row-click);
   *   if state has no `id` (direct URL navigation), fetches the entity from the API via `getById`.
   */
  protected loadEntity(): void {
    console.log('BaseComponent.loadEntity() called');
    if (!this.initEditMode()) return;
    const state = history.state;
    if (state?.['id']) {
      this.populateForm(state);
    } else {

      this.loading = true;

      this.getService().getById(
        this.entityId!,
        (data) => {
          this.loading = false;
          this.populateForm(data);
          this.cdr.markForCheck();
        },
        (err) => {
          this.loading = false;
          this.errorMessage = err?.status === 0 ? 'Server Error' : err?.error?.message || 'Failed to load.';
          this.cdr.markForCheck();
        }
      );
    }
  }

  /**
   * Reads the `:id` route parameter and sets `isEditMode` and `entityId`.
   * @returns `true` if an id was found (edit-mode), `false` if not (add-mode).
   */
  protected initEditMode(): boolean {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return false;
    this.isEditMode = true;
    this.entityId = +id;
    return true;
  }

  /** Navigates back to the entity list page (`listUrl`). */
  goBack(): void {
    this.router.navigate([this.listUrl]);
  }

  /**
   * Validates the form and submits the entity to the API.
   * - Edit-mode: calls `update` on the service.
   * - Add-mode: calls `add` on the service.
   * On success navigates to the list. On failure calls `handleSaveError`.
   */
  onSave(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || this.saving) return;
    this.saving = true;
    this.errorMessage = '';
    const body = this.getBody();
    if (this.isEditMode && this.entityId) {
      this.getService().update(this.entityId, body,
        () => this.goBack(),
        (err) => this.handleSaveError(err)
      );
    } else {
      this.getService().add(body, () => this.goBack(), (err) => this.handleSaveError(err));
    }
  }

  /**
   * Deletes the current entity via the service.
   * On success navigates to the list. On failure calls `handleDeleteError`.
   */
  onDelete(): void {
    if (!this.entityId || this.deleting) return;
    this.deleting = true;
    this.errorMessage = '';
    this.getService().delete(this.entityId, () => this.goBack(), (err) => this.handleDeleteError(err));
  }

  /**
   * Resets `saving` and sets `errorMessage` after a failed save request.
   * @param err - The HTTP error response from the API.
   */
  protected handleSaveError(err: any): void {
    this.saving = false;
    this.errorMessage = err?.status === 0 ? 'Server Error' : (err?.error?.message || 'Save failed. Please try again.');
    this.errors = (err?.error?.result?.inputerror || {});
    console.log('handleSaveError', err);
    this.cdr.markForCheck();
  }

  /**
   * Resets `deleting` and sets `errorMessage` after a failed delete request.
   * @param err - The HTTP error response from the API.
   */
  protected handleDeleteError(err: any): void {
    this.deleting = false;
    this.errorMessage = err?.status === 0 ? 'Server Error' : (err?.error?.message || 'Delete failed. Please try again.');
    this.cdr.markForCheck();
  }
}
