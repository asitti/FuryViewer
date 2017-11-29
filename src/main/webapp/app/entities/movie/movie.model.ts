import { BaseEntity } from './../../shared';

export class Movie implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public releaseDate?: any,
        public description?: string,
        public imgContentType?: string,
        public img?: any,
        public duration?: number,
        public director?: BaseEntity,
        public scriptwriter?: BaseEntity,
        public company?: BaseEntity,
        public actors?: BaseEntity[],
        public reviews?: BaseEntity[],
        public favoriteMovies?: BaseEntity[],
        public rateMovies?: BaseEntity[],
        public stats?: BaseEntity[],
        public hatedMovies?: BaseEntity[],
        public socials?: BaseEntity[],
        public genres?: BaseEntity[],
    ) {
    }
}