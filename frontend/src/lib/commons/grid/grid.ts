import { Component, booleanAttribute, input, output } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Skeleton } from '../skeleton/skeleton';

export interface GridColumn {
  field: string;
  title: string;
}

@Component({
  selector: 'app-grid',
  imports: [Skeleton],
  templateUrl: './grid.html',
  styleUrl: './grid.scss',
})
export class Grid extends BaseComponent {
  readonly title = input<string>('');

  readonly columns = input<GridColumn[]>([]);

  readonly rows = input<any[]>([]);

  readonly emptyText = input<string>('Kayıt yok');

  readonly clickable = input(false, { transform: booleanAttribute });

  readonly rowClicked = output<any>();
}
