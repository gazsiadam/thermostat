import {Injectable} from '@angular/core';
import {RxStomp} from "@stomp/rx-stomp";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private rxStomp: RxStomp;

  constructor() {
    this.rxStomp = new RxStomp();
    this.rxStomp.configure({
      brokerURL: `${DataService.getUrl()}/websocket`,
      heartbeatOutgoing: 1000,
      forceBinaryWSFrames: false,
      splitLargeFrames: false
    });
    this.rxStomp.activate();
  }

  temperature(): Observable<any> {
    return this.rxStomp.watch({
      destination: "/temperature"
    });
  }

  activeProgram(): Observable<any> {
    return this.rxStomp.watch({
      destination: "/active-program"
    });
  }

  systemHealth(): Observable<any> {
    return this.rxStomp.watch({
      destination: "/system-health"
    });
  }

  logs(): Observable<any> {
    return this.rxStomp.watch({
      destination: "/log"
    });
  }

  private static getUrl(): string {
    const server = window.location.hostname === 'localhost' ? 'localhost:8082' : window.location.hostname;
    return `ws://${server}/data`;
  }

}
