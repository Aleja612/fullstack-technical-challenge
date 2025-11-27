export interface Product {
    id: string;
    name: string;
    description: string;
    price: number;
}

export interface InventoryStatus {
    id: string;
    productName: string;
    quantity: number;
    status: string;
}