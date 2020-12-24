import {BrowserModule, HAMMER_GESTURE_CONFIG, HammerGestureConfig, HammerModule} from '@angular/platform-browser';
import {Injectable, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from "@angular/material/button";
import {FormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {HttpClientModule} from "@angular/common/http";
import {FlexLayoutModule} from "@angular/flex-layout";
import {ManualTemperatureDialogComponent} from './component/manual-temperature-dialog/manual-temperature-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatIconModule} from "@angular/material/icon";
import {TemperatureComponent} from './component/temperature/temperature.component';
import {MatTabsModule} from "@angular/material/tabs";
import * as Hammer from 'hammerjs';
import { LogComponent } from './component/log/log.component';

@Injectable()
export class MyHammerConfig extends HammerGestureConfig {
  overrides = <any>{
    swipe: {direction: Hammer.DIRECTION_ALL},
  };
}

@NgModule({
  declarations: [
    AppComponent,
    TemperatureComponent,
    ManualTemperatureDialogComponent,
    LogComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HammerModule,
    FormsModule,
    HttpClientModule,
    FlexLayoutModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatDialogModule,
    MatTabsModule
  ],
  providers: [
    {
      provide: HAMMER_GESTURE_CONFIG,
      useClass: MyHammerConfig,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
