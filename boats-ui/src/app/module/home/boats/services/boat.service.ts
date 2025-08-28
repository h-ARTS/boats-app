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

  private _boats = signal<Boat[]>([]);
  boats = this._boats.asReadonly();

  constructor() {
    this.loadBoats();
  }

  private loadBoats() {
    this.http
      .get<{ content: Boat[] }>(GET_BOATS, {
        headers: { Authorization: `Bearer ${this.auth.getToken()}` },
      })
      .subscribe((data) => this._boats.set(data.content as Boat[]));
  }

  fetchBoat(id: number): Observable<Boat | null> {
    return this.http.get<Boat>(GET_BOAT.replace('{id}', id.toString()), {
      headers: { Authorization: `Bearer ${this.auth.getToken()}` },
    });
  }

  getBoat(id: number): Observable<Boat | null> {
    const boat = this._boats().find((b) => b.id === id) ?? null;
    if (boat) {
      return of(boat);
    } else {
      return this.fetchBoat(id).pipe(
        map((b) => {
          if (b) {
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
    this._boats.update((bs) => [...bs, boat]);
    return of(boat);
  }

  updateBoat(id: number, update: Partial<Boat>): Observable<Boat | null> {
    let updated: Boat | null = null;
    this._boats.update((bs) =>
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

  deleteBoat(id: number) {
    this._boats.update((bs) => bs.filter((b) => b.id !== id));
    this.http
      .delete(DELETE_BOAT.replace('{id}', id.toString()), {
        headers: { Authorization: `Bearer ${this.auth.getToken()}` },
      })
      .subscribe();
  }
}
