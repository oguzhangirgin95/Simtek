import { Component, computed, input, output } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-pagination',
  imports: [],
  templateUrl: './pagination.html',
  styleUrl: './pagination.scss',
})
export class Pagination extends BaseComponent {
  readonly totalCount = input<number>(0);

  readonly pageNumber = input<number>(1);

  readonly pageSize = input<number>(20);

  readonly pageChange = output<number>();

  readonly pageCount = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));

  /** Gecerli sayfanin etrafindaki en fazla bes sayfa */
  readonly pages = computed(() => {
    const first = Math.max(1, Math.min(this.pageNumber() - 2, this.pageCount() - 4));
    const last = Math.min(this.pageCount(), first + 4);

    const list: number[] = [];
    for (let page = first; page <= last; page++) {
      list.push(page);
    }
    return list;
  });

  readonly info = computed(() => {
    const from = (this.pageNumber() - 1) * this.pageSize() + 1;
    const to = Math.min(this.pageNumber() * this.pageSize(), this.totalCount());

    return this.totalCount() === 0
      ? this.getResource('PAGE_EMPTY', 'Kayıt yok')
      : `${from} - ${to} / ${this.totalCount()}`;
  });

  go(page: number): void {
    if (page >= 1 && page <= this.pageCount() && page !== this.pageNumber()) {
      this.pageChange.emit(page);
    }
  }
}
