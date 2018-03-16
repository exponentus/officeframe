export class Tag {
    id: string = '';
    name: string;
    color: string;
    locName: any;
    hidden: boolean;

    static convertToDtoList(tags: Tag[]): any {
        return tags ? tags.map(it => { return { id: it.id }; }) : [];
    }
}
