import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { AuthService } from '../../../../shared/auth/auth.service';
import {
  Boat,
  DELETE_BOAT,
  GET_BOAT,
  GET_BOATS,
  POST_BOATS,
  PUT_BOAT,
} from '../../../../shared/types';

@Injectable({ providedIn: 'root' })
export class BoatService {
  private http = inject(HttpClient);
  private auth = inject(AuthService);

  boats = signal<Boat[]>([]);
  currentBoat = signal<Boat | null>(null);

  loadBoats() {
    this.http
      .get<{ content: Boat[] }>(GET_BOATS, {
        headers: { Authorization: `Bearer ${this.auth.getToken()}` },
      })
      .subscribe((data) => this.boats.set(data.content as Boat[]));
  }

  fetchBoat(id: number): Observable<Boat | null> {
    return this.http.get<Boat>(GET_BOAT.replace('{id}', id.toString()), {
      headers: { Authorization: `Bearer ${this.auth.getToken()}` },
    });
  }

  getBoats(): Observable<Boat[]> {
    return of(this.boats());
  }

  getBoat(id: number): Observable<Boat | null> {
    const boat = this.boats().find((b) => b.id === id) ?? null;
    if (boat) {
      this.currentBoat.set(boat);
      return of(boat);
    } else {
      return this.fetchBoat(id).pipe(
        map((b) => {
          if (b) {
            this.currentBoat.set(b);
            return b;
          }
          return null;
        }),
        catchError((val) => of(val))
      );
    }
  }

  // Add, update, delete stubs
  addBoat(boat: Boat): Observable<Boat> {
    this.http
      .post<Boat>(POST_BOATS, boat, {
        headers: { Authorization: `Bearer ${this.auth.getToken()}` },
      })
      .subscribe();
    this.boats.update((bs) => [...bs, boat]);
    return of(boat);
  }

  updateBoat(id: number, update: Partial<Boat>): Observable<Boat | null> {
    let updated: Boat | null = null;
    this.boats.update((bs) =>
      bs.map((b) => {
        if (b.id === id) {
          updated = { ...b, ...update };
          return updated;
        }
        return b;
      })
    );
    this.http
      .put<Boat>(PUT_BOAT.replace('{id}', id.toString()), update, {
        headers: { Authorization: `Bearer ${this.auth.getToken()}` },
      })
      .subscribe();
    return of(updated);
  }

  deleteBoat(id: number): Observable<boolean> {
    this.boats.update((bs) => bs.filter((b) => b.id !== id));
    this.http
      .delete(DELETE_BOAT.replace('{id}', id.toString()), {
        headers: { Authorization: `Bearer ${this.auth.getToken()}` },
      })
      .subscribe();
    return of(true);
  }
}
