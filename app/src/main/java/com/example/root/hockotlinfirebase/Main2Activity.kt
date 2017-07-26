package com.example.root.hockotlinfirebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    lateinit var arrString: ArrayList<String>
    lateinit var arrKey: ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>
    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        arrString = ArrayList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrString)
        listView.adapter = adapter
        arrKey = ArrayList()

        myRef = FirebaseDatabase.getInstance().getReference()
        myRef.child("Name").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val key: String = p0?.key.toString()
                val name: String = p0?.getValue().toString()
                for (item in arrKey.indices) {
                    if (key.contains(arrKey[item])) {
                        arrString.removeAt(item)
                        adapter.notifyDataSetChanged()
                        arrString.add(item, name)
                        adapter.notifyDataSetChanged()
//                        Toast.makeText(this@Main2Activity,""+arrKey[item] + "\n" + key, Toast.LENGTH_SHORT).show()
//                        break
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val name: String = p0?.getValue().toString()
                val key: String = p0?.key.toString()
                arrString.add(name)
                arrKey.add(key)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                val name: String = p0?.getValue().toString()
                val key: String = p0?.key.toString()
                arrString.remove(name)
                arrKey.remove(key)
                adapter.notifyDataSetChanged()
            }

        })

        xoaitem()
        addControls()
        suaitem()

    }

    private fun suaitem() {
        listView.setOnItemClickListener { adapterView, view, i, l ->
            edt.setText(arrString[i])
            btnEdit.setOnClickListener {
                arrString.removeAt(i)
                arrString.add(i,edt.text.toString())
                adapter.notifyDataSetChanged()

//                myRef.child("Name").child(arrKey[i]).removeValue()
                val map: HashMap<String,String> = HashMap<String, String>()
                map.put("/Name/" + arrKey[i], edt.text.toString())

                myRef.updateChildren(map as Map<String, Any>?)
            }
        }
    }

    private fun xoaitem() {
        listView.setOnItemLongClickListener { adapterView, view, i, l ->
            myRef.child("Name").child(arrKey[i]).removeValue()
            true
        }
    }

    private fun addControls() {
        btn.setOnClickListener {
            val name = edt.text.toString()
            myRef.child("Name").push().setValue(name)
            edt.setText("")
        }
    }
}
