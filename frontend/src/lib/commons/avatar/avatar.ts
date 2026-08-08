import { Component, computed, input } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';

@Component({
  selector: 'app-avatar',
  imports: [],
  templateUrl: './avatar.html',
  styleUrl: './avatar.scss',
})
export class Avatar extends BaseComponent {
  readonly src = input<string>('');

  readonly name = input<string>('');

  readonly size = input<number>(48);

  readonly initials = computed(() =>
    this.name()
      .split(' ')
      .filter((part) => part.length > 0)
      .slice(0, 2)
      .map((part) => part[0].toUpperCase())
      .join(''),
  );
}
