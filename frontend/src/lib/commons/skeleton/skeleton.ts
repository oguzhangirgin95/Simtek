import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-skeleton',
  imports: [],
  templateUrl: './skeleton.html',
  styleUrl: './skeleton.scss',
})
export class Skeleton {
  readonly lines = input<number>(1);

  readonly height = input<number>(14);

  readonly rows = computed(() => Array.from({ length: this.lines() }, (value, index) => index));
}
