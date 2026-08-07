import { Component, computed } from '@angular/core';
import { BaseComponent } from '../../base/basecomponent/basecomponent';

@Component({
  selector: 'app-body',
  imports: [],
  templateUrl: './body.html',
  styleUrl: './body.scss',
})
export class Body extends BaseComponent {
  readonly loadingText = computed(() => this.getResource('LOADING', 'Yükleniyor...'));

  readonly backText = computed(() => this.getResource('BUTTON_BACK', 'Geri'));

  readonly continueText = computed(() => this.getResource('BUTTON_CONTINUE', 'Devam'));
}
