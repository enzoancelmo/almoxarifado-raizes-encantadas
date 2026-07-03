import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({selector:'app-placeholder',standalone:true,templateUrl:'./placeholder.component.html',styleUrl:'./placeholder.component.css'})
export class PlaceholderComponent {
  readonly title = this.route.snapshot.data['title'] as string;
  readonly description = this.route.snapshot.data['description'] as string;
  readonly icon = this.route.snapshot.data['icon'] as string;
  constructor(private readonly route: ActivatedRoute) {}
}
