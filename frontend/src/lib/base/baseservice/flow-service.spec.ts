import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { BaseComponent } from '../basecomponent/base-component';
import { FlowService } from './flow-service';

@Component({ template: '' })
class TestComponent extends BaseComponent {}

describe('FlowService + State proxy', () => {
  let flowService: FlowService;
  let component: TestComponent;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [TestComponent] });
    flowService = TestBed.inject(FlowService);
    component = TestBed.createComponent(TestComponent).componentInstance;
  });

  it('yazilan alan geri okunur', () => {
    component.State.deneme = 5;
    expect(component.State.deneme).toBe(5);
  });

  it('component ve servis ayni state i paylasir', () => {
    component.State.deneme = 'merhaba';
    expect(flowService.get<string>('deneme')).toBe('merhaba');

    flowService.set('deneme', 'gunaydin');
    expect(component.State.deneme).toBe('gunaydin');
  });

  it('select ile signal reaktif calisir', () => {
    const sayi = flowService.select<number>('sayi');
    expect(sayi()).toBeUndefined();

    component.State.sayi = 42;
    expect(sayi()).toBe(42);
  });

  it('yazilmamis alan undefined doner', () => {
    expect(component.State.olmayan).toBeUndefined();
  });

  it('clear state i temizler', () => {
    component.State.a = 1;
    flowService.clear();
    expect(component.State.a).toBeUndefined();
  });
});
