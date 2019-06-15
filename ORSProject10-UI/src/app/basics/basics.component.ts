import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-basics',
  templateUrl: './basics.component.html',
  styleUrls: ['./basics.component.css']
})

export class BasicsComponent implements OnInit {

  title = '';
  subtitle = '';
  money = 100;

  month = 'No Month';
  monthList = ["January", "Feburary", "March", "April", "May",
    "June", "July", "August", "September",
    "October", "November", "December"];
  personlist = [
    { name: 'Ram', age: '30', city: 'Mumbai' },
    { name: 'Shyam', age: '25', city: 'Delhi' },
    { name: 'Vishnu', age: '40', city: 'Indore' }
  ];

  todaydate = new Date();
  
  constructor() {
  }

  ngOnInit() {
    this.title = 'SunilOS';
    this.subtitle = 'Open Source Learning Center';
  }

  changeMonth(event){
    this.month = event.​srcElement.value;
  }

}
