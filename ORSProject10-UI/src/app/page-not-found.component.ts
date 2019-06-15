import { Component } from '@angular/core';

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./app.component.css']
})

export class PageNotFoundComponent {
  message = 'Page not found, URL may be invalid';
}
