import { provideZonelessChangeDetection } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { beforeEach, describe, expect, it } from 'vitest';
import { DashboardStart } from './dashboard.start';

describe('DashboardStart harita basligi', () => {
  let fixture: ComponentFixture<DashboardStart>;
  let component: DashboardStart;

  beforeEach(async () => {
    TestBed.configureTestingModule({
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    fixture = TestBed.createComponent(DashboardStart);
    component = fixture.componentInstance;

    // ngOnInit'in yaptigi gibi istegi hazirla (servis cagrilari testte bos gecer).
    component.State.Request = { cityId: '', unitId: '', status: '' };
    await fixture.whenStable();
  });

  /** Harita kartinin basligi (panels icindeki ilk kart). */
  function renderedTitle(): string {
    return fixture.nativeElement.querySelector('.panels app-card h3')?.textContent?.trim() ?? '';
  }

  function clickCard(index: number): void {
    const cards = fixture.nativeElement.querySelectorAll('.stats .app-statcard');
    cards[index].click();
  }

  it('baslangicta varsayilan baslik gorunur', () => {
    expect(renderedTitle()).toBe('Şehir bazlı toplam memur');
  });

  it('karta tiklayinca ekrandaki baslik degisir', async () => {
    clickCard(0);
    await fixture.whenStable();
    expect(renderedTitle()).toBe('Şehir bazlı toplam memur');

    clickCard(3);
    await fixture.whenStable();
    expect(renderedTitle()).toBe('Şehir bazlı izinde olan memur');

    clickCard(5);
    await fixture.whenStable();
    expect(renderedTitle()).toBe('Şehir bazlı limiti aşan memur');
  });

  it('ayni karta tekrar tiklayinca varsayilana doner', async () => {
    clickCard(1);
    await fixture.whenStable();
    expect(renderedTitle()).toBe('Şehir bazlı sahada olan memur');

    clickCard(1);
    await fixture.whenStable();
    expect(renderedTitle()).toBe('Şehir bazlı toplam memur');
  });

  it('secili metrigin rengi haritaya gecer', async () => {
    clickCard(1);
    await fixture.whenStable();

    const map = fixture.nativeElement.querySelector('app-map .app-map');
    expect(map.className).toContain('app-map--success');
  });
});
