export interface HistoryType {
    id: number,
    name: string,
    description?: string;
    color: string;
    defautl: boolean
}
export interface History {
    id: number;
    type: number;
    money: number;
    description?: string;
    date: string;
    history_type: number;
}
export interface userProfile {
    id: number;
    signature?: String;
    name: string;
    firstName: String;

    lastName?: String;

    company?: String;

    usr?: String;

    email: String;

    address?: String;

    city?: String;

    country?: String;

    postalCode?: String;

    bio?: String;

    lastUpdate?: String;

    income?: number;

    expend?: number;

    avatar?:string;
}
