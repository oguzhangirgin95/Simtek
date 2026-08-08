import { Component, computed, signal } from '@angular/core';
import { BaseComponent } from '@lib/base/basecomponent/basecomponent';
import { Palette } from '@lib/base/baseservice/themeservice';

@Component({
  selector: 'app-theme',
  imports: [],
  templateUrl: './theme.html',
  styleUrl: './theme.scss',
})
export class Theme extends BaseComponent {
  readonly palettes = this.themeService.palettes;

  readonly current = this.themeService.current;

  readonly open = signal(false);

  readonly active = computed(
    () => this.palettes.find((palette) => palette.code === this.current()) ?? this.palettes[0],
  );

  readonly labels = computed(() => ({
    theme: this.getResource('THEME_TITLE', 'Tema'),
    reset: this.getResource('THEME_RESET', 'Varsayılan temaya dön'),
  }));

  toggle(): void {
    this.open.update((value) => !value);
  }

  close(): void {
    this.open.set(false);
  }

  select(code: string): void {
    this.themeService.apply(code);
    this.close();
  }

  reset(): void {
    this.themeService.reset();
    this.close();
  }

  isDefault(): boolean {
    return this.themeService.isDefault();
  }

  swatch(palette: Palette): string {
    return `linear-gradient(135deg, ${palette.colors['--color-surface']} 50%, ${palette.colors['--color-accent']} 50%)`;
  }
}
