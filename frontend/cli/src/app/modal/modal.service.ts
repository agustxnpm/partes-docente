import { Injectable } from "@angular/core";
import { NgbActiveModal, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ModalComponent } from "./modal.component";

@Injectable({
  providedIn: "root",
})
export class ModalService {
  constructor(private modalService: NgbModal) {}

  confirm(title: string, message: string, description: string): Promise<any> {
    const modalRef = this.modalService.open(ModalComponent, {
      backdrop: "static",
      keyboard: false,
      centered: true,
      size: "md",
      windowClass: "modal-container",
    });
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.description = description;
    modalRef.componentInstance.isSingleButton = false;
    return modalRef.result;
  }

  alert(title: string, message: string): Promise<void> {
    const modalRef = this.modalService.open(ModalComponent, {
      backdrop: "static",
      keyboard: false,
      centered: true,
      size: "md",
      windowClass: "modal-container",
    });
    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.description = "";
    modalRef.componentInstance.isSingleButton = true;
    return modalRef.result;
  }
}
