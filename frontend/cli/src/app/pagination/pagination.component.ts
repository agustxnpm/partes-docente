import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, Output, SimpleChanges } from "@angular/core";

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  imports: [CommonModule],
  styleUrls: ['./styles-pagination.css'],
  standalone: true
})
export class PaginationComponent {
  @Input() totalPages: number = 0;
  @Input() last: boolean = false;
  @Input() currentPage: number = 1;
  @Input() number: number = 1;
  @Output() pageChangeRequested = new EventEmitter<number>();
  pages: (number | string)[] = [];

  constructor() {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['totalPages'] || changes['currentPage']) {
      this.pages = this.generateVisiblePages(this.currentPage, this.totalPages);
    }
  }

  onPageChange(pageId: number | string): void {
    if (typeof pageId !== 'number') return;
  
    if (!pageId || pageId < 1 || pageId > this.totalPages || pageId === this.currentPage) return;
  
    this.currentPage = pageId;
    this.pageChangeRequested.emit(pageId);
  }
  

  private generateVisiblePages(current: number, total: number): (number | string)[] {
    const delta = 2;
    const range: number[] = [];
    const rangeWithDots: (number | string)[] = [];
    let last: number | undefined;

    for (let i = 1; i <= total; i++) {
      if (i === 1 || i === total || (i >= current - delta && i <= current + delta)) {
        range.push(i);
      }
    }

    for (let i of range) {
      if (last !== undefined) {
        if (i - last === 2) {
          rangeWithDots.push(last + 1);
        } else if (i - last > 2) {
          rangeWithDots.push('...');
        }
      }
      rangeWithDots.push(i);
      last = i;
    }

    return rangeWithDots;
  }
}
