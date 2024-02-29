<?php

defined('BASEPATH') OR exit('No direct script access allowed');

Class Mstore extends CI_Model {

	public function getSignup() {
		$check_name = $this->db->get_where('store_mst', array('sm_name' => $_POST['sName'],));
		$check_email = $this->db->get_where('store_mst', array('sm_email' => $_POST['sEmail']));

		if ($check_name->num_rows() > 0 || $check_email->num_rows() > 0 ) {
			return false;
		} else {

			$this->load->helper('string');
			$this->load->library('encryption');

			$psw = $this->encryption->encrypt($_POST['sPassword']);

			$image = substr($_POST['sImage'], strpos($_POST['sImage'], ",") + 1);
			$img = base64_decode($image);
			$dir = "";
			$uploadfile = time() . ".jpeg";
			file_put_contents("uploads/" . $uploadfile, $img);
			$name = $uploadfile;

			$data = array(
				'sm_name' => $_POST['sName'],
				'sm_email' => $_POST['sEmail'],
				'sm_password' => $psw,
				'sm_contact' => $_POST['sMobile'],
				'sm_address' => $_POST['sAddress'],
				'sm_img' => $name,
				'sm_date' => date("Y-m-d"),
			);

			$store = $this->db->insert('store_mst', $data);
			$last_id = $this->db->insert_id();

			$insert_check = $this->db->get_where('store_mst', array('sm_id' => $last_id, 'sm_name' => $_POST['sName'], 'sm_email' => $_POST['sEmail']))->row_array();

			if (!empty($insert_check)) {
				$insert_data = array(
					'sm_id' => $insert_check['sm_id'],
					'sm_name' => $insert_check['sm_name'],
					'sm_email' => $insert_check['sm_email'],
					'sm_contact' => $insert_check['sm_contact'],
					'sm_address' => $insert_check['sm_address'],
					'sm_img' => $insert_check['sm_img'],
					'sm_latitude' => $insert_check['sm_latitude'],
					'sm_longitude' => $insert_check['sm_longitude'],
					'sm_status' => $insert_check['sm_status'],
					'sm_date' => date("d-m-Y", strtotime($insert_check['sm_date'])),
				);
				return $insert_data;
			} else {
				return false;
			}
		}
	}
	
	public function getLatLng(){
		$data = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId'],'sm_latitude' => $_POST['sLat'],'sm_longitude' => $_POST['sLng']))->row();

		if(!empty($data)){
			$d = array(
				'status'=>'true',
				'sm_latitude'=>$data->sm_latitude,
				'sm_longitude'=>$data->sm_longitude,
			);
			return $d;
		}else{
			$update = $this->db->update('store_mst',array('sm_latitude' => $_POST['sLat'],'sm_longitude' => $_POST['sLng'],'sm_status' => 1 ),array('sm_id' => $_POST['sId'],));
			
			$update_data = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId'],'sm_latitude' => $_POST['sLat'],'sm_longitude' => $_POST['sLng']))->row(); 
			
			if(!empty($update_data)){
				$d = array(
					'status'=>'true',
					'sm_latitude'=>$update_data->sm_latitude,
					'sm_longitude'=>$update_data->sm_longitude,
				);
				return $d;
			}else{
				return false;
			}
		}
		
	}
	
	public function getlogin(){
		$this->load->library('encryption');
		$store = $this->db->get_where('store_mst',array('sm_email' => $_POST['sEmail'],));
		
		if ($store->num_rows() > 0) {
			$dp = $this->encryption->decrypt($store->row()->sm_password);

			if($dp == $_POST['sPassword']){
				$data = array(
					'sm_id' => $store->row()->sm_id,
					'sm_name' => $store->row()->sm_name,
					'sm_email' => $store->row()->sm_email,
					'sm_contact' => $store->row()->sm_contact,
					'sm_address' => $store->row()->sm_address,
					'sm_img' => $store->row()->sm_img,
					'sm_latitude' => $store->row()->sm_latitude,
					'sm_longitude' => $store->row()->sm_longitude,
					'sm_status' => $store->row()->sm_status,
					'sm_date' => date("d-m-Y", strtotime($store->row()->sm_date)),
				);
				return $data;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}

	public function getDetails(){
		$store = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId'],));
		
		if ($store->num_rows() > 0) {
			
			$to = $this->db->get_where('order_mst',array('om_store_id' => $_POST['sId'],))->result();
			if(!empty($to)){
				$total_order = count($to);
			}else{
				$total_order = '0';
			}
			
			$po = $this->db->get_where('order_mst',array('om_store_id' => $_POST['sId'],'om_status' => 0))->result();
			if(!empty($po)){
				$pending_order = count($po);
			}else{
				$pending_order = '0';
			}
			
			$coo = $this->db->get_where('order_mst',array('om_user_id' => $_POST['sId'],'om_status' => 1))->result();
			if(!empty($coo)){
				$complete_order = count($coo);
			}else{
				$complete_order = '0';
			}

			$cao = $this->db->get_where('order_mst',array('om_user_id' => $_POST['sId'],'om_status' => 2))->result();
			if(!empty($cao)){
				$cancel_order = count($cao);
			}else{
				$cancel_order = '0';
			}
			
			$data = array(
				'sm_id' => $store->row()->sm_id,
				'sm_name' => $store->row()->sm_name,
				'sm_email' => $store->row()->sm_email,
				'sm_contact' => $store->row()->sm_contact,
				'sm_address' => $store->row()->sm_address,
				'sm_img' => $store->row()->sm_img,
				'sm_latitude' => $store->row()->sm_latitude,
				'sm_longitude' => $store->row()->sm_longitude,
				'sm_status' => $store->row()->sm_status,
				'sm_date' => date("d-m-Y", strtotime($store->row()->sm_date)),
				'total_order' => $total_order,
				'pending_order' => $pending_order,
				'complete_order' => $complete_order,
				'cancel_order' => $cancel_order,
			);
			return $data;
		} else {
			return false;
		}
	}

	public function getList(){
		$store = $this->db->get_where('store_mst',array('sm_status' => 1 ));
		$data = array();
		if ($store->num_rows() > 0) {
			foreach($store->result() as $s){
				$d = array(
					'sm_id' => $s->sm_id,
					'sm_name' => $s->sm_name,
					'sm_email' => $s->sm_email,
					'sm_contact' => $s->sm_contact,
					'sm_address' => $s->sm_address,
					'sm_img' => $s->sm_img,
					'sm_latitude' => $s->sm_latitude,
					'sm_longitude' => $s->sm_longitude,
					'sm_status' => $s->sm_status,
					'sm_date' => date("d-m-Y", strtotime($s->sm_date)),
				);
				array_push($data, $d);
			}
			return $data;
		} else {
			return false;
		}
	}
	
	public function getProductAdd(){

		$check_store = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId'], ));
		if($check_store->num_rows() > 0) {
			$image = substr($_POST['pImage'], strpos($_POST['pImage'], ",") + 1);
			$img = base64_decode($image);
			$dir = "";
			$uploadfile = time() . ".jpeg";
			file_put_contents("uploads/" . $uploadfile, $img);
			$name = $uploadfile;

			$data = array(
				'pm_store_id' => $_POST['sId'],
				'pm_name' => $_POST['pName'],
				'pm_description' => $_POST['pDesc'],
				'pm_qty' => $_POST['pQty'],
				'pm_price' => $_POST['pAmount'],
				'pm_img' => $name,
				'pm_date' => date("Y-m-d"),
			);

			$product = $this->db->insert('product_mst', $data);
			$last_id = $this->db->insert_id();
			
			if(!empty($last_id)){ 
				return true;
			}else{
				return false;
			}	
		}
		else{
			return false;
		}
	}

	public function getProductList(){
		$this->db->order_by('pm_id', 'desc');
		$store_product = $this->db->get_where('product_mst',array('pm_store_id' => $_GET['sId'],));
		
		if ($store_product->num_rows() > 0) {
			$d = array();
			
			foreach ($store_product->result() as $sp) {
				$data = array(
					'pm_id' => $sp->pm_id,
					'pm_store_id' => $sp->pm_store_id,
					'pm_name' => $sp->pm_name,// edit
					'pm_img' => $sp->pm_img,// edit and delete both side
					'pm_price' => $sp->pm_price,// edit
					'pm_qty' => $sp->pm_qty,// edit with add new qty
					'pm_description' => $sp->pm_description,// edit
					'pm_date' => date("d-m-Y", strtotime($sp->pm_date)),
				);
				array_push($d,$data);
			}
			
			return $d;
		} else {
			return false;
		}
	}

	public function getProductDelete(){
		$product_delete = $this->db->delete('product_mst',array('pm_id' => $_POST['pId'],'pm_store_id' => $_POST['sId']));

		$check_product = $this->db->get_where('product_mst',array('pm_id' => $_POST['pId'],'pm_store_id' => $_POST['sId']));
		if ($check_product->num_rows() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public function getProductEdit(){
		$check_product = $this->db->get_where('product_mst',array('pm_id' => $_POST['pId'],'pm_store_id' => $_POST['sId']));
		if ($check_product->num_rows() > 0) {

			if(!empty($_POST['pImage'])){
				if ($check_product->row()->pm_img != '') {
					if (file_exists('uploads/' . $check_product->row()->pm_img)) {
						unlink('uploads/' . $check_product->row()->pm_img);
					}
				}
				
				$image = substr($_POST['pImage'], strpos($_POST['pImage'], ",") + 1);
				$img = base64_decode($image);
				$dir = "";
				$uploadfile = time() . ".jpeg";
				file_put_contents("uploads/" . $uploadfile, $img);
				$name = $uploadfile;

				$data = array(
					'pm_name' => $_POST['pName'],
					'pm_description' => $_POST['pDesc'],
					'pm_qty' => $_POST['pQty'],
					'pm_price' => $_POST['pAmount'],
					'pm_img' => $name,
				);

			}else{
				$data = array(
					'pm_name' => $_POST['pName'],
					'pm_description' => $_POST['pDesc'],
					'pm_qty' => $_POST['pQty'],
					'pm_price' => $_POST['pAmount'],
				);
				
			}	

			if(!empty($data)){
				$update = $this->db->update('product_mst', $data, array('pm_id' => $_POST['pId'],'pm_store_id' => $_POST['sId']));

				return true;
			}else{
				return false;
			}	
		} else{
			return false;

		}
	}

	public function getEditProfile(){
		$check_store = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId'],));
		if ($check_store->num_rows() > 0) {

			if(!empty($_POST['sImage'])){
				if ($check_store->row()->sm_img != '') {
					if (file_exists('uploads/' . $check_store->row()->sm_img)) {
						unlink('uploads/' . $check_store->row()->sm_img);
					}
				}
				
				$image = substr($_POST['sImage'], strpos($_POST['sImage'], ",") + 1);
				$img = base64_decode($image);
				$dir = "";
				$uploadfile = time() . ".jpeg";
				file_put_contents("uploads/" . $uploadfile, $img);
				$name = $uploadfile;

				if($check_store->row()->sm_latitude == $_POST['sLat'] && $check_store->row()->sm_longitude == $_POST['sLog']){
					$data = array(
						'sm_name' => $_POST['sName'],
						'sm_contact' => $_POST['sMobile'],
						'sm_address' => $_POST['sAddress'],
						'sm_img' => $name,
					);	
				}else{
					$data = array(
						'sm_name' => $_POST['sName'],
						'sm_contact' => $_POST['sMobile'],
						'sm_address' => $_POST['sAddress'],
						'sm_latitude' => $_POST['sLat'],
						'sm_longitude' => $_POST['sLog'],
						'sm_img' => $name,
					);
				}

			}else{
				if($check_store->row()->sm_latitude == $_POST['sLat'] && $check_store->row()->sm_longitude == $_POST['sLog']){
					$data = array(
						'sm_name' => $_POST['sName'],
						'sm_contact' => $_POST['sMobile'],
						'sm_address' => $_POST['sAddress'],
					);	
				}else{
					$data = array(
						'sm_name' => $_POST['sName'],
						'sm_contact' => $_POST['sMobile'],
						'sm_address' => $_POST['sAddress'],
						'sm_latitude' => $_POST['sLat'],
						'sm_longitude' => $_POST['sLog'],
					);
				}
			}	

			if(!empty($data)){
				$update = $this->db->update('store_mst', $data, array('sm_id' => $_POST['sId']));

				$store = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId'],))->row();
				$data = array(
					'sm_id' => $store->sm_id,
					'sm_name' => $store->sm_name,
					'sm_email' => $store->sm_email,
					'sm_contact' => $store->sm_contact,
					'sm_address' => $store->sm_address,
					'sm_img' => $store->sm_img,
					'sm_latitude' => $store->sm_latitude,
					'sm_longitude' => $store->sm_longitude,
					'sm_status' => $store->sm_status,
					'sm_date' => date("d-m-Y", strtotime($store->sm_date)),
				);
				return $data;
			}else{
				return false;
			}	
		} else{
			return false;

		}
	}

}
