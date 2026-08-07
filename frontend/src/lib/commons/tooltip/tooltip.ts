import { Component, input } from '@angular/core';

export interface TooltipState {
  text: string;
  x: number;
  y: number;
}

export const TOOLTIP_HIDDEN: TooltipState = { text: '', x: 0, y: 0 };

@Component({
  selector: 'app-tooltip',
  imports: [],
  templateUrl: './tooltip.html',
  styleUrl: './tooltip.scss',
})
export class Tooltip {
  readonly state = input<TooltipState>(TOOLTIP_HIDDEN);
}
