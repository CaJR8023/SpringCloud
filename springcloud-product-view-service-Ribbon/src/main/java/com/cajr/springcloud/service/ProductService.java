package com.cajr.springcloud.service;

import com.cajr.springcloud.client.ProductClientRibbon;
import com.cajr.springcloud.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductClientRibbon productClientRibbon;

    public List<Product> listProducts(){
        return productClientRibbon.listProducts();
    }
}
