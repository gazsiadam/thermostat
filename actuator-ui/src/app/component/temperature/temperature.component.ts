import {Component} from '@angular/core';
import {ThermostatService} from "../../service/thermostat.service";
import {MatDialog} from "@angular/material/dialog";
import {ManualTemperatureDialogComponent} from "../manual-temperature-dialog/manual-temperature-dialog.component";
import {DataService} from "../../service/data.service";

@Component({
  selector: 'app-temperature',
  templateUrl: './temperature.component.html',
  styleUrls: ['./temperature.component.scss']
})
export class TemperatureComponent {

  temperature: number;
  status;
  systemHealth = {
    color: 'green',
    icon: 'check'
  };

  constructor(private thermostatService: ThermostatService,
              private dataService: DataService,
              private dialog: MatDialog) {
    this.thermostatService.getStatus().subscribe(status => {
      this.status = status;
      this.getSystemHealth();
    });
    this.dataService.temperature().subscribe((message) => {
      this.temperature = message?.body;
    });
  }

  setManualTemperature() {
    const dialogRef = this.dialog.open(ManualTemperatureDialogComponent, {
      height: '100vh',
      maxHeight: '100vh',
      width: '100vw',
      maxWidth: '100vw',
      data: {
        temperature: this.status?.programSchedule?.manualModeTemperature
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.thermostatService.setManualMode(result.temperature).subscribe(() => {
          this.status.programSchedule.manualMode = true;
          this.status.programSchedule.manualModeTemperature = result.temperature;
        });
      }
    });
  }

  cancelManualTemperature() {
    this.thermostatService.clearManualMode().subscribe(() => {
      this.status.programSchedule.manualMode = false;
    });
  }

  getDate(start: any) {
    return Date.now();
  }

  getSystemHealth() {
    if (this.status.systemStatus.systemHealth === 'ERROR') {
      this.systemHealth.color = 'red';
      this.systemHealth.icon = 'error'
    } else if (this.status.systemStatus.systemHealth === 'WARN') {
      this.systemHealth.color = 'orange';
      this.systemHealth.icon = 'warning'
    }
  }

}
