import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "app-modal",
  imports: [CommonModule],
  templateUrl: "modal.component.html",
  styleUrl: "modal-styles.css",
})
export class ModalComponent {
  constructor(public modal: NgbActiveModal) {}

  @Input() title: string = "";
  @Input() message: string = "";
  @Input() description: string = "";
  @Input() isSingleButton: boolean = false;
}
