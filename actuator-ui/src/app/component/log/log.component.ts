import {Component, OnInit} from '@angular/core';
import {LogService} from "../../service/log.service";
import {DataService} from "../../service/data.service";

@Component({
  selector: 'app-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.scss']
})
export class LogComponent implements OnInit {

  constructor(public logService: LogService,
              private dataService: DataService) {
  }

  ngOnInit(): void {
    this.dataService.logs().subscribe(message => {
      this.logService.addLog(message.body);
    });
  }

}
