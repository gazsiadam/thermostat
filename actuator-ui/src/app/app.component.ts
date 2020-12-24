import {Component} from '@angular/core';
import {MatTabGroup} from "@angular/material/tabs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  swipeLeft(tabGroup: MatTabGroup) {
    tabGroup.selectedIndex = Math.min(tabGroup._tabs.length, tabGroup.selectedIndex + 1);
  }

  swipeRight(tabGroup: MatTabGroup) {
    tabGroup.selectedIndex = Math.min(tabGroup._tabs.length, tabGroup.selectedIndex - 1);
  }
}
