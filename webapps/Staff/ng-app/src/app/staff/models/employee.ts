import { Organization } from './organization';
import { Department } from './department';

export class Employee {
    id: string;
    isNew: boolean;
    userID: string;
    login: string;
    email: string;
    name: string = '@anonymous';
    iin: string;
    rank: number;
    organization: Organization;
    department: Department;
    position: any;
    allRoles: string[];
    roles: any[];
    user: any;
    avatarURL: string;

    static convertToDto(m: Employee) {
        return {
            id: m.id,
            name: m.name,
            iin: m.iin,
            rank: m.rank,
            organization: m.organization ? { id: m.organization.id } : null,
            department: m.department ? { id: m.department.id } : null,
            position: m.position ? { id: m.position.id } : null,
            roles: m.roles ? m.roles.map(it => { return { id: it.id }; }) : [],
            user: { login: m.login }
        };
    }
}
