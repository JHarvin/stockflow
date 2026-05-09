import { Routes } from '@angular/router';
import { MovementsComponent } from './components/movements/movements';
import { DashboardComponent } from './components/dashboard/dashboard';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'movements', component: MovementsComponent },
  { path: '**', redirectTo: 'dashboard' }
];
