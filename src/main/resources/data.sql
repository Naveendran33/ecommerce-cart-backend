-- Insert Categories                                                                                                                          
INSERT INTO categories (name, slug, description, created_at) VALUES
                                                                 ('Electronics', 'electronics', 'Gadgets and gear', CURRENT_TIMESTAMP),
                                                                 ('Apparel', 'apparel', 'Clothing and fashion', CURRENT_TIMESTAMP);

-- Insert Products
INSERT INTO products (name, slug, description, price, stock_quantity, is_active, category_id, created_at, updated_at) VALUES
                                                                                                                          ('Wireless Headphones', 'wireless-headphones', 'Noise cancelling', 199.99, 50, true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          ('Gaming Mouse', 'gaming-mouse', 'RGB mechanical mouse', 59.99, 100, true, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          ('Cotton T-Shirt', 'cotton-tshirt', '100% Cotton', 19.99, 200, true, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
                                                                                                                     
-- Insert Coupons
INSERT INTO coupons (code, coupon_type, discount_value, min_order_amount, is_active, created_at) VALUES
('SAVE20', 'PERCENTAGE', 20.00, 100.00, true, CURRENT_TIMESTAMP),
('MINUS50', 'FIXED_AMOUNT', 50.00, 200.00, true, CURRENT_TIMESTAMP);