import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-manual-temperature-dialog',
  templateUrl: './manual-temperature-dialog.component.html',
  styleUrls: ['./manual-temperature-dialog.component.scss']
})
export class ManualTemperatureDialogComponent {

  private interval = 150;

  increaseHandler;
  decreaseHandler;

  constructor(public dialogRef: MatDialogRef<ManualTemperatureDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  increaseStart(event) {
    this.increase();
    this.increaseHandler = setInterval(() => {
      this.increase();
    }, this.interval);
    event?.stopPropagation();
    event?.preventDefault();
  }

  decreaseStart(event) {
    this.decrease();
    this.decreaseHandler = setInterval(() => {
      this.decrease();
    }, this.interval);
    event?.stopPropagation();
    event?.preventDefault();
  }

  stop(handler) {
    if (handler) {
      clearInterval(handler);
    }
  }

  private increase() {
    this.data.temperature = Math.round((this.data.temperature + 0.1) * 10) / 10;
  }

  private decrease() {
    this.data.temperature = Math.round((this.data.temperature - 0.1) * 10) / 10;
  }
}
