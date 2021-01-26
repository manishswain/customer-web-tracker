package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO 
{
	//need to inject the session factory
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Customer> getCustomers() 
	{
		//get the current hibernate session
		Session currentSession=sessionFactory.getCurrentSession();
		
		//create a query
		Query<Customer> theQuery=currentSession.createQuery("from Customer order by lastName",Customer.class);
		
		//execute the query and get the result list
		List<Customer> customers =theQuery.getResultList();
		
		//return the results		
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) 
	{
		//get current hibernate session
		Session currentSession=sessionFactory.getCurrentSession();
		
		//save or update the customer to the database
		currentSession.saveOrUpdate(theCustomer);
		
	}

	@Override
	public Customer getCustomers(int theId) 
	{
		//get current hibernate session
		Session currentSession=sessionFactory.getCurrentSession();
		
		//now retrieve from the databse using the primary key
		Customer theCustomer=currentSession.get( Customer.class, theId);

		return theCustomer;
	}

	@Override
	public void deleteCustomer(int theId)
	{
		//get current hibernate session
		Session currentSession=sessionFactory.getCurrentSession();
		
		//delete the object by primary key of the customer
		Query theQuery=currentSession.createQuery("delete from Customer where id=:customerId");
		
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();				
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		//get current hibernate session
		Session currentSession=sessionFactory.getCurrentSession();
		
		Query theQuery=null;
		//only search by name if the search name is not empty
		if(theSearchName!=null && theSearchName.trim().length()>0)
		{
			//search for firstName or lastName ....case insensitive
			theQuery=currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName",Customer.class);
			
			theQuery.setParameter("theName","%"+ theSearchName.toLowerCase()+"%");
		}
		else
		{
			theQuery=currentSession.createQuery("from Customer order by lastName",Customer.class);
		}
		
		//execute query and get the result list
		List<Customer> customers =theQuery.getResultList();
		
		return customers;
	}

}
