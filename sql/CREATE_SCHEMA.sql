CREATE TABLE impressions
(
    product_id int,
    click      bool,
    "date"     date
);

CREATE TABLE products
(
    product_id  int,
    category_id int,
    price       int
);

CREATE TABLE purchases
(
    product_id int,
    user_id    int,
    "date"     date
);