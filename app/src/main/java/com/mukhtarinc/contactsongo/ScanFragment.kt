package com.mukhtarinc.contactsongo

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaMetadata
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.iterator
import androidx.databinding.DataBindingUtil
import com.bitvale.lavafab.Child
import com.mukhtarinc.contactsongo.databinding.FragmentScanBinding
import kotlinx.android.synthetic.main.fragment_scan.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [ScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "ScanFragment"

lateinit var  _context: Context
var cols = listOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
    ContactsContract.CommonDataKinds.Phone.NUMBER,
    ContactsContract.CommonDataKinds.Phone._ID).toTypedArray()

lateinit var binding: FragmentScanBinding

val handler : Handler = Handler()
class ScanFragment : Fragment() {





    // TODO: Rename and change types of parameters



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _context = context!!

         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_scan,container,false)

        with(binding.lavaFab){


            setParentOnClickListener { lava_fab.trigger() }
            setChildOnClickListener(Child.TOP){Toast.makeText(_context,"Export As VCF",Toast.LENGTH_SHORT).show()}
            setChildOnClickListener(Child.LEFT){Toast.makeText(_context,"Send to Cloud",Toast.LENGTH_SHORT).show()}

        }



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loadContacts()







    }


    override fun onDestroy() {
        super.onDestroy()
        binding.pulsator.stop()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ScanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if(requestCode==101 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                syncContacts()

        }
    }


    fun loadContacts() {




        binding.pulsator.start()


        var progressStatus = 0

        val cursor : Cursor? = syncContacts()

        binding.progress.max = cursor?.count!!

        Thread(Runnable {

            run {

                while (progressStatus< binding.progress.max){


                    progressStatus += 1
                    handler.post {
                        run {

                            binding.progress.progress = progressStatus


                            cursor.moveToNext()

                            binding.name.text = cursor.getString(0)



                        }
                    }

                    Thread.sleep(200)
                }




            }



        }).start()






    }



    fun syncContacts() : Cursor?{


        return _context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                        cols,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

    }