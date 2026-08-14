import { ChangeDetectorRef, Directive, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BaseService } from '../services/base.service';
import { FormGroup } from '@angular/forms';

@Directive()
export abstract class BaseListComponent implements OnInit {

  form!: FormGroup;

  /** Holds the raw response from the preload API; available to child components. */
  preloadData: any;

  protected abstract buildForm(): FormGroup;

  searchForm: any = {
    error: false,
    message: '',
    listdata: [],
    data: {},
    pageNo: 0,
    pageSize: 10
  };
  loading = true;

  /** Route path of the list page — used by `goBack()` and after save/delete. */
  protected abstract pageUrl: string;



  constructor(protected router: Router, protected cdr: ChangeDetectorRef) { }

  /**
   * Clears the loading state and forces change detection to reflect the latest `searchForm`.
   */
  protected refresh(): void {
    this.loading = false;
    this.cdr.detectChanges();
  }

  /**
   * Loads dropdown/reference data for the form via the entity's service, if supported.
   */
  protected loadDropdowns(): void {
    // alert('calling  base loadDropdowns');
    this.getService().preload((r: any) => {
      this.preloadData = r.data;
      this.cdr.markForCheck();
    }, () => { });
  }

  /**
   * Navigates back to the welcome/home page.
   */
  goBack(): void {
    this.router.navigate(['/welcome']);
  }

  /**
   * Navigates to an arbitrary route.
   * @param url - Route path to navigate to.
   */
  forward(url: string): void {
    this.router.navigate([url]);
  }

  /**
   * Navigates to the edit page for the given entity, passing it via router state.
   * @param entity - Entity to edit; must include an `id`.
   */
  edit(entity: any): void {
    this.router.navigate([this.pageUrl, entity.id], { state: entity });
  }

  /**
   * Navigates to the next page of results.
   */
  next() {
    this.searchForm.pageNo++;
    this.doSearch();
  }

  /**
    * Navigates to the previous page of results.
    */
  previous() {
    if (this.searchForm.pageNo > 1) {
      this.searchForm.pageNo--;
    }
    this.doSearch();
  }

  /**
    * Returns the service instance responsible for CRUD operations on this entity.
    * Called by `onSave()`, `onDelete()`, and `loadEntity()`.
    */
  protected abstract getService(): BaseService;

  /**
   * Loads dropdown data and runs the initial search when the component is created.
   */
  ngOnInit(): void {
    this.loadDropdowns();
    this.doSearch();
  }

  /**
   * Executes the search API call with the current form values.
   * When `reportType` is `'html'` (the default), updates `searchForm` with the JSON result set.
   * Otherwise, requests a binary report from the server and triggers a file download.
   * @param reportType - `'html'` for the normal JSON search, or `'pdf'`/`'doc'` to export a report.
   */
  protected doSearch(reportType: string = 'html'): void {
    this.loading = true;
    let body = this.form.value;
    // Remove empty values from the body
    Object.keys(body).forEach(key => {
      if (body[key] === '' || body[key] === null) {
        delete body[key];
      }
    });

    //add searchForm.pageNo and pageSize to body
    body.pageNo = this.searchForm.pageNo;
    body.pageSize = this.searchForm.pageSize;
    body.reportType = reportType;

    const onError = (err: any) => {
      this.searchForm.error = true;
      this.searchForm.message = err?.status === 0 ? 'Server Error' : (err?.error?.message || 'Failed to load.');
      this.refresh();
    };

    if (reportType !== 'html') {
      this.getService().searchReport(body,
        (blob: Blob) => {
          this.downloadBlob(blob, reportType === 'pdf' ? 'report.pdf' : 'report.docx');
          this.refresh();
        },
        onError
      );
      return;
    }

    this.getService().search(body,
      (response: any) => {
        this.searchForm.listdata = response.result.data;
        this.searchForm.error = !response.success;
        this.searchForm.message = response.message;
        this.refresh();
      },
      onError
    );
  }

  /**
   * Triggers a browser download of the given Blob using a temporary anchor element.
   * @param blob - File content to download.
   * @param filename - Filename to save the download as.
   */
  private downloadBlob(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }

}
