package com.lcwd.electronic.store.dtos;

public class CartItemDto {
	
	 	private int cartItemId;
	    private ProductDto product;
	    private int quantity;
		private double totalPrice;
		public int getCartItemId() {
			return cartItemId;
		}
		public void setCartItemId(int cartItemId) {
			this.cartItemId = cartItemId;
		}
		public ProductDto getProduct() {
			return product;
		}
		public void setProduct(ProductDto product) {
			this.product = product;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public double getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(double totalPrice) {
			this.totalPrice = totalPrice;
		}
	    
}

