import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Header } from './header/header';
import { Body } from './body/body';
import { Footer } from './footer/footer';
import { Button } from './button/button';
import { Input } from './input/input';
import { Select } from './select/select';
import { Info } from './info/info';
import { Badge } from './badge/badge';
import { Avatar } from './avatar/avatar';
import { Card } from './card/card';
import { Statcard } from './statcard/statcard';
import { List } from './list/list';
import { Grid } from './grid/grid';
import { Detailcard } from './detailcard/detailcard';
import { Progress } from './progress/progress';
import { Barchart } from './barchart/barchart';
import { Donutchart } from './donutchart/donutchart';
import { Map } from './map/map';
import { Menu } from './menu/menu';
import { Tabs } from './tabs/tabs';
import { Theme } from './theme/theme';
import { Modal } from './modal/modal';
import { Spinner } from './spinner/spinner';
import { Validation } from './validation/validation';

const COMPONENTS = [
  Header,
  Body,
  Footer,
  Button,
  Input,
  Select,
  Info,
  Badge,
  Avatar,
  Card,
  Statcard,
  List,
  Grid,
  Detailcard,
  Progress,
  Barchart,
  Donutchart,
  Map,
  Menu,
  Tabs,
  Theme,
  Modal,
  Spinner,
  Validation,
];

@NgModule({
  imports: [CommonModule, ...COMPONENTS],
  exports: [...COMPONENTS],
})
export class CommonsModule {}
