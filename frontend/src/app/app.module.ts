import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { CompaniesComponent } from './companies/companies.component';
import { CompanyDetailsComponent } from './company-details/company-details.component';
import { MessagesComponent } from './messages/messages.component';
import { AppRoutingModule } from './/app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { CompanyComponent } from './company/company.component';

@NgModule({
  declarations: [ AppComponent, CompaniesComponent, CompanyDetailsComponent, MessagesComponent, DashboardComponent, CompanyComponent ],
  imports: [ BrowserModule, FormsModule, AppRoutingModule, HttpClientModule ],
  providers: [ ],
  bootstrap: [AppComponent]
})
export class AppModule { }
