package co.mvvm_dagger_iteo.domain

import co.mvvm_dagger_iteo.data.models.Person

class Person(mPerson:Person){
     val id:Int? = mPerson.id
     val _id:String? = mPerson?._id
     val birth_date: String = mPerson.birth_date
     val first_name: String= mPerson.first_name
     val last_name: String = mPerson.last_name
     val sex: String = mPerson.sex
 }