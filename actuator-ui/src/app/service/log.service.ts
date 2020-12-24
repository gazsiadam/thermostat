import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LogService {

  logs: any[] = [];

  constructor() {
  }

  addLog(log) {
    this.logs.unshift(JSON.parse(log));
    if (this.logs.length > 1000) {
      this.logs.shift();
    }
  }
}
