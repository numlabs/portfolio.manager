import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompaniesComponent } from './companies/companies.component'
import { CompanyComponent } from './company/company.component'
import { DashboardComponent }   from './dashboard/dashboard.component';

const routes: Routes = [
  { path: 'companies/:exchange', component: CompaniesComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'company/:operation', component: CompanyComponent },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  declarations: [ ],
  exports: [RouterModule]
})
export class AppRoutingModule { 


}
