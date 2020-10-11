package com.example.ass8mysql

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    var employeeList :ArrayList<Employee> = arrayListOf<Employee>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.adapter = EmployeeAdapter(this.employeeList,applicationContext)
        recycler_view.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL))

        recycler_view.addOnItemTouchListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                Toast.makeText(applicationContext, "Click on : "+employeeList[position].name,
                    Toast.LENGTH_SHORT).show()
            }

        })

    }
    override fun onResume(){
        super.onResume()
        callEmployeedata()
    }
    fun callEmployeedata() {
        employeeList.clear()
        val serv: EmployAPI = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmployAPI::class.java)

        serv.retrieveEmployee()
            .enqueue(object : Callback<List<Employee>> {
                override fun onResponse(
                    call: Call<List<Employee>>,
                    response: Response<List<Employee>>
                ){
                    response.body()?.forEach {
                        employeeList.add(Employee(it.name,it.gender,it.email,it.salary))
                    }
                    recycler_view.adapter = EmployeeAdapter(employeeList,applicationContext)
                    employee.text = "Employee List: " + employeeList.size.toString() + " Employee"
                }
                override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                    return t.printStackTrace()
                } } )
    }
    fun onClickAddEmployee(v: View) {
        val intent = Intent(this@MainActivity, InsertActivity::class.java)
        startActivity(intent)

    }
    interface  OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }
    fun RecyclerView.addOnItemTouchListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {

            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }
}