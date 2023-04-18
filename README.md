# Karos-Activation-Middleman
A middleman service for Karos Economy meant for Activating Products


Make sure to define the following variables when starting this up:

-DAPIUrl=domain.karos.org -DAPIUser=seller@karos.org -DAPIPassword=sellerPassword

Once started simply use one of the following commands:

product - Tell the system you want to add/update a product (Product name is used)
reset - Reset someones activations based on Order ID

Example validation API:

http://127.0.0.1:9191/validate?productname=DONOTBUY&orderid=20230418-13472990&uuid=12342

I suggest putting this entire thing behind an NGINX reverse proxy and setup HTTPS .. DO NOT USE HTTP unless you want to be insecure.