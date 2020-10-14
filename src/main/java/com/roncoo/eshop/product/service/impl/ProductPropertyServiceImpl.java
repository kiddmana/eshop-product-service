package com.roncoo.eshop.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roncoo.eshop.product.mapper.ProductPropertyMapper;
import com.roncoo.eshop.product.model.ProductProperty;
import com.roncoo.eshop.product.model.ProductSpecification;
import com.roncoo.eshop.product.rabbitmq.RabbitMQSender;
import com.roncoo.eshop.product.rabbitmq.RabbitQueue;
import com.roncoo.eshop.product.service.ProductPropertyService;

@Service
public class ProductPropertyServiceImpl implements ProductPropertyService {

	@Autowired
	private ProductPropertyMapper productPropertyMapper;
	
	@Autowired
	private RabbitMQSender rabbitMQSender;
	
	public void add(ProductProperty productProperty, String operationType) {
		productPropertyMapper.add(productProperty); 
		String queue = null;
		if(operationType == null || "".equals(operationType)){
			queue = RabbitQueue.DATA_CHANGE_QUEUE;
		} else if("refresh".equals(operationType)){
			queue = RabbitQueue.REFRESH_DATA_CHANGE_QUEUE;
		} else if("high".equals(operationType)){
			queue = RabbitQueue.HIGH_PRIORITY_DATA_CHANGE_QUEUE;
		}
		rabbitMQSender.send(queue, "{\"event_type\": \"add\", \"data_type\": \"product_property\", \"id\": " + productProperty.getId() + ", \"product_id\": " + productProperty.getProductId()+ "}");

	}

	public void update(ProductProperty productProperty, String operationType) {
		productPropertyMapper.update(productProperty); 
		String queue = null;
		if(operationType == null || "".equals(operationType)){
			queue = RabbitQueue.DATA_CHANGE_QUEUE;
		} else if("refresh".equals(operationType)){
			queue = RabbitQueue.REFRESH_DATA_CHANGE_QUEUE;
		} else if("high".equals(operationType)){
			queue = RabbitQueue.HIGH_PRIORITY_DATA_CHANGE_QUEUE;
		}
		rabbitMQSender.send(queue, "{\"event_type\": \"update\", \"data_type\": \"product_property\", \"id\": " + productProperty.getId() + ", \"product_id\": " + productProperty.getProductId()+"}");

	}

	public void delete(Long id, String operationType) {
		ProductProperty productProperty = findById(id);
		productPropertyMapper.delete(id); 
		String queue = null;
		if(operationType == null || "".equals(operationType)){
			queue = RabbitQueue.DATA_CHANGE_QUEUE;
		} else if("refresh".equals(operationType)){
			queue = RabbitQueue.REFRESH_DATA_CHANGE_QUEUE;
		} else if("high".equals(operationType)){
			queue = RabbitQueue.HIGH_PRIORITY_DATA_CHANGE_QUEUE;
		}
		rabbitMQSender.send(queue, "{\"event_type\": \"delete\", \"data_type\": \"product_property\", \"id\": " + id + ", \"product_id\": " + productProperty.getProductId()+"}");

	}

	public ProductProperty findById(Long id) {
		return productPropertyMapper.findById(id);
	}

	@Override
	public List<ProductProperty> findByIds(String ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductProperty findByProductId(Long productId) {
		// TODO Auto-generated method stub
		return productPropertyMapper.findById(productId);
	}

}
