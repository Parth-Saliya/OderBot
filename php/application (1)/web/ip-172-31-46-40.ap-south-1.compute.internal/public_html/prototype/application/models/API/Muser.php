<?php

defined('BASEPATH') OR exit('No direct script access allowed');

Class Muser extends CI_Model {

	public function getSignup() {
		$check_email = $this->db->get_where('user_mst', array('um_email' => $_POST['uEmail']));

		if ($check_email->num_rows() > 0 ) {
			return false;
		} else {

			$this->load->helper('string');
			$this->load->library('encryption');

			$psw = $this->encryption->encrypt($_POST['uPassword']);

			$image = substr($_POST['uImage'], strpos($_POST['uImage'], ",") + 1);
			$img = base64_decode($image);
			$dir = "";
			$uploadfile = time() . ".jpeg";
			file_put_contents("uploads/" . $uploadfile, $img);
			$name = $uploadfile;

			$data = array(
				'um_f_name' => $_POST['uFirstName'],
				'um_l_name' => $_POST['uLastName'],
				'um_email' => $_POST['uEmail'],
				'um_password' => $psw,
				'um_contact' => $_POST['uMobile'],
				'um_img' => $name,
				'um_date' => date("Y-m-d"),
			);

			$store = $this->db->insert('user_mst', $data);
			$last_id = $this->db->insert_id();

			$insert_check = $this->db->get_where('user_mst', array('um_id' => $last_id, 'um_email' => $_POST['uEmail']))->row_array();

			if (!empty($insert_check)) {
				$insert_data = array(
					'um_id' => $insert_check['um_id'],
					'um_f_name' => $insert_check['um_f_name'],
					'um_l_name' => $insert_check['um_l_name'],
					'um_email' => $insert_check['um_email'],
					'um_contact' => $insert_check['um_contact'],
					'um_img' => $insert_check['um_img'],
					'um_date' => date("d-m-Y", strtotime($insert_check['um_date'])),
				);
				return $insert_data;
			} else {
				return false;
			}
		}
	}

	public function getlogin(){
		$this->load->library('encryption');
		$user = $this->db->get_where('user_mst',array('um_email' => $_POST['uEmail'],));
		
		if ($user->num_rows() > 0) {
			$dp = $this->encryption->decrypt($user->row()->um_password);

			if($dp == $_POST['uPassword']){
				$data = array(
					'um_id' => $user->row()->um_id,
					'um_f_name' => $user->row()->um_f_name,
					'um_l_name' => $user->row()->um_l_name,
					'um_email' => $user->row()->um_email,
					'um_contact' => $user->row()->um_contact,
					'um_img' => $user->row()->um_img,
					'um_date' => date("d-m-Y", strtotime($user->row()->um_date)),
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
		$user = $this->db->get_where('user_mst',array('um_id' => $_POST['uId'],));
		
		if ($user->num_rows() > 0) {

			$to = $this->db->get_where('order_mst',array('om_user_id' => $_POST['uId'],))->result();
			if(!empty($to)){
				$total_order = count($to);
			}else{
				$total_order = '0';
			}
			
			$po = $this->db->get_where('order_mst',array('om_user_id' => $_POST['uId'],'om_status' => 0))->result();
			if(!empty($po)){
				$pending_order = count($po);
			}else{
				$pending_order = '0';
			}
			
			$coo = $this->db->get_where('order_mst',array('om_user_id' => $_POST['uId'],'om_status' => 1))->result();
			if(!empty($coo)){
				$complete_order = count($coo);
			}else{
				$complete_order = '0';
			}

			$cao = $this->db->get_where('order_mst',array('om_user_id' => $_POST['uId'],'om_status' => 2))->result();
			if(!empty($cao)){
				$cancel_order = count($cao);
			}else{
				$cancel_order = '0';
			}


			$data = array(
				'um_id' => $user->row()->um_id,
				'um_f_name' => $user->row()->um_f_name,
				'um_l_name' => $user->row()->um_l_name,
				'um_email' => $user->row()->um_email,
				'um_contact' => $user->row()->um_contact,
				'um_img' => $user->row()->um_img,
				'um_date' => date("d-m-Y", strtotime($user->row()->um_date)),
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

	public function getProductList(){ 
		$check_store = $this->db->get_where('store_mst',array('sm_latitude' => $_GET['sLat'],'sm_longitude' => $_GET['sLog'], ));

		if($check_store->num_rows() > 0){
			$this->db->order_by('pm_id', 'desc');
			$store_product = $this->db->get_where('product_mst',array('pm_store_id' => $check_store->row()->sm_id,));
			
			$a = array(
				'status' => 'true',
				'sm_id' => $check_store->row()->sm_id,
				'sm_name' => $check_store->row()->sm_name,
				'sm_email' => $check_store->row()->sm_email,
				'sm_contact' => $check_store->row()->sm_contact,
				'sm_address' => $check_store->row()->sm_address,
				'sm_img' => $check_store->row()->sm_img,
				'sm_latitude' => $check_store->row()->sm_latitude,
				'sm_longitude' => $check_store->row()->sm_longitude,
				'sm_status' => $check_store->row()->sm_status,
				'sm_date' => date("d-m-Y", strtotime($check_store->row()->sm_date)),	
			);

			if ($store_product->num_rows() > 0) {
				$d = array();
				
				foreach ($store_product->result() as $sp) {
					$data = array(
						'pm_id' => $sp->pm_id,
						'pm_store_id' => $sp->pm_store_id,
						'pm_name' => $sp->pm_name,
						'pm_img' => $sp->pm_img,
						'pm_price' => $sp->pm_price,
						'pm_qty' => $sp->pm_qty,
						'pm_description' => $sp->pm_description,
						'pm_date' => date("d-m-Y", strtotime($sp->pm_date)),
					);
					array_push($d,$data);
				}

				$a['data'] = $d;	
				return $a;
			} else {
				return false;
			}	
		}else{
			return false;
		}
		
	}

	public function getEditProfile(){
		$check_user = $this->db->get_where('user_mst',array('um_id' => $_POST['uId'],));
		if ($check_user->num_rows() > 0) {

			if(!empty($_POST['uImage'])){
				if ($check_user->row()->um_img != '') {
					if (file_exists('uploads/' . $check_user->row()->um_img)) {
						unlink('uploads/' . $check_user->row()->um_img);
					}
				}
				
				$image = substr($_POST['uImage'], strpos($_POST['uImage'], ",") + 1);
				$img = base64_decode($image);
				$dir = "";
				$uploadfile = time() . ".jpeg";
				file_put_contents("uploads/" . $uploadfile, $img);
				$name = $uploadfile;

				$data = array(
					'um_f_name' => $_POST['uFirstName'],
					'um_l_name' => $_POST['uLastName'],
					'um_contact' => $_POST['uMobile'],
					'um_img' => $name,
				);

			}else{
				$data = array(
					'um_f_name' => $_POST['uFirstName'],
					'um_l_name' => $_POST['uLastName'],
					'um_contact' => $_POST['uMobile'],
				);
				
			}	

			if(!empty($data)){
				$update = $this->db->update('user_mst', $data, array('um_id' => $_POST['uId']));

				$user = $this->db->get_where('user_mst',array('um_id' => $_POST['uId'],))->row();
				$data = array(
					'um_id' => $user->um_id,
					'um_f_name' => $user->um_f_name,
					'um_l_name' => $user->um_l_name,
					'um_email' => $user->um_email,
					'um_contact' => $user->um_contact,
					'um_img' => $user->um_img,
					'um_date' => date("d-m-Y", strtotime($user->um_date)), 
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
