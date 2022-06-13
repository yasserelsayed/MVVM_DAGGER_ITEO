package co.mvvm_dagger_iteo.domain
import co.mvvm_dagger_iteo.R
import  co.mvvm_dagger_iteo.data.models.Car
import co.mvvm_dagger_iteo.util.Constants
import java.text.SimpleDateFormat

class Car(private val mCar:Car) {
    constructor(brand: String,
                color: String,
                lat: Double,
                lng: Double,
                model: String,
                ownerId: String,
                registration: String,
                year: String
    ):this(Car(brand,color,lat,lng,model,ownerId,registration,year))
    val id = mCar._id
    val brand = mCar.brand
    val color =  mCar.color
    val lat =  mCar.lat
    val lng =  mCar.lng
    val model =  mCar.model
    val ownerId =  mCar.ownerId
    val registration =  mCar.registration
   private val year =  mCar.year
    val yearFormated:String
    get() {
        return if(year?.length>6) toDate.format( fromDate.parse(year))
             else year
        }
    var synced =  mCar.synced

    private var owner:Person?=null
    private val fromDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
    private val toDate = SimpleDateFormat("yyyy")
    fun getDataObj():Car = mCar

    fun attachOwner(p:Person?){
        owner = p
    }

    val ownerFullname:String
    get() {
       return owner?.let {
            "${it.first_name} ${it.last_name}"
        }?:""
    }



    fun validateModel():HashMap<String,Int>{
        val ret = HashMap<String,Int>()

        if(brand.isEmpty())ret[Constants.DATATAGS.brand.name] = R.string.er_brand_field_required
        if(color.isEmpty()) ret[Constants.DATATAGS.color.name] = R.string.er_brand_field_required
        if(lat<=0.0)ret[Constants.DATATAGS.lat.name] = R.string.er_lat_field_required
        if(lng<=0.0)ret[Constants.DATATAGS.lng.name] = R.string.er_lng_field_required
        if(model.isEmpty()) ret[Constants.DATATAGS.model.name] = R.string.er_model_field_required
        if(ownerId.isEmpty()) ret[Constants.DATATAGS.ownerId.name] = R.string.er_owner_field_required
        if(registration.isEmpty()) ret[Constants.DATATAGS.registration.name] = R.string.er_owner_field_required
        if(year.isEmpty())ret[Constants.DATATAGS.year.name] = R.string.er_year_field_required
        if(ownerId.isEmpty()) ret[Constants.DATATAGS.ownerId.name] = R.string.er_year_field_required

        return ret
    }
}