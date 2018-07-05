import { Component, OnInit } from '@angular/core';
import { CompanyService } from '../service/company.service'

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  status: string = '';
  updateButtontText = 'Update Prices';

  constructor(private companyService: CompanyService) {}

  updatePrices() { 
    this.updateButtontText = 'updating...';
    this.companyService.updateCompanyPrices('bist').subscribe(
      e=>{
        status=e;
        this.updateButtontText = 'Update completed ';

        setTimeout(() => {
          this.updateButtontText = 'Update Prices';
        },
        3000);
        
      });    
  }  

  ngOnInit() {
  }

}
