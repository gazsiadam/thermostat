import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, timer} from "rxjs";
import {environment} from "../../environments/environment";
import {switchMap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ThermostatService {

  private backendUrl: string = environment.backendUrl;

  constructor(private http: HttpClient) {
  }

  getStatus(): Observable<number> {
    return timer(0, 15000).pipe(
      switchMap(() => this.http.get<number>(`${this.backendUrl}/status`))
    );
  }

  getSensorTemperature(): Observable<number> {
    return this.http.get<number>(`${this.backendUrl}/sensor-temperature`);
  }

  setManualMode(temperature: number) {
    return this.http.get<void>(`${this.backendUrl}/set-manual-mode?manualTemperature=${temperature}`);
  }

  clearManualMode() {
    return this.http.get<void>(`${this.backendUrl}/clear-manual-mode`);
  }
}
