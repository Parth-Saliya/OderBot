<?php

defined('BASEPATH') OR exit('No direct script access allowed');

Class Morder extends CI_Model {

	public function getOrder(){
		$store = $this->db->get_where('store_mst',array('sm_id' => $_POST['sId']))->row();
		$user = $this->db->get_where('user_mst',array('um_id' => $_POST['uId']))->row();

		if(!empty($store) && !empty($user)){
			$store_details = array(
				'sm_id' => $store->sm_id,
				'sm_name' => $store->sm_name,
				'sm_email' => $store->sm_email,
				'sm_contact' => $store->sm_contact,
				'sm_address' => $store->sm_address,
				'sm_img' => $store->sm_img,
				'sm_latitude' => $store->sm_latitude,
				'sm_longitude' => $store->sm_longitude,
				'sm_status' => $store->sm_status,
				'sm_date' => $store->sm_date,
			);

			$user_details = array(
				'um_id' => $user->um_id,
				'um_f_name' => $user->um_f_name,
				'um_l_name' => $user->um_l_name,
				'um_email' => $user->um_email,
				'um_contact' => $user->um_contact,
				'um_img' => $user->um_img,
				'um_date' => $user->um_date,
			);

			$items = count(json_decode($_POST['pDetail']));
			$qtys = 0;
			$qty = 0; 
			foreach (json_decode($_POST['pDetail']) as $pd) {
				$qty = $qty + $pd->pQty;
				$product_qty = $this->db->get_where('product_mst',array('pm_id' => $pd->pId))->row();

				$qty_minus = $product_qty->pm_qty - $pd->pQty;

				if($qty_minus < 0){
					$qtys = 0;
				}else{
					$qtys = $qtys + $pd->pQty;
				}
			}

			if($qty == $qtys){

				foreach (json_decode($_POST['pDetail']) as $pd) {
					$product_qty = $this->db->get_where('product_mst',array('pm_id' => $pd->pId))->row();
					$qty_minus = $product_qty->pm_qty - $pd->pQty;
					$product_qty_update =$this->db->update('product_mst',array('pm_qty' => $qty_minus),array('pm_id' => $pd->pId));
				}

				$order = array(
					'om_order_id' => rand(99999,999999),
					'om_store_id' => $_POST['sId'],
					'om_user_id' => $_POST['uId'],
					'om_details' => $_POST['pDetail'],
					'om_user_details' => json_encode($user_details),
					'om_store_details' => json_encode($store_details),
					'om_total' => $_POST['fAmount'],
					'om_status' => '0',
					'om_date' => date('Y-m-d'),
				);

				$order_insert = $this->db->insert('order_mst', $order);
				$last_id = $this->db->insert_id();

				if(!empty($last_id)){
					$get_order_detail = $this->db->get_where('order_mst',array('om_id' => $last_id))->row();
					if(!empty($get_order_detail)){

						$order_details = array(
							'o_id' => $get_order_detail->om_id,
							'order_id' => $get_order_detail->om_order_id,
							'total_item' => strval($items),
							'total_qty' => strval($qtys),
							'order_amount' => $get_order_detail->om_total,
							'order_status' => $get_order_detail->om_status ,
							'order_date' => date("d-m-Y", strtotime($get_order_detail->om_date)),
						);

						return $order_details;

					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public function getuOrderList(){
		$this->db->order_by('om_id', 'desc');
		$order = $this->db->get_where('order_mst',array('om_user_id' => $_GET['uId'],))->result();

		if(!empty($order)){
			$d = array();
			foreach($order as $o){
				$data = array(
					'o_id' => $o->om_id,
					'order_id' => $o->om_order_id,
					'total_amount' => $o->om_total,
					'o_status' => $o->om_status,
					'o_date' => date("d-m-Y", strtotime($o->om_date)),
				);

				array_push($d, $data);
			}
			return $d;	
		}else{
			return false;
		}
	}

	public function getsOrderList(){
		$this->db->order_by('om_id', 'desc');
		$order = $this->db->get_where('order_mst',array('om_store_id' => $_GET['sId'],))->result();

		if(!empty($order)){
			$d = array();
			foreach($order as $o){
				$data = array(
					'o_id' => $o->om_id,
					'order_id' => $o->om_order_id,
					'total_amount' => $o->om_total,
					'o_status' => $o->om_status,
					'o_date' => date("d-m-Y", strtotime($o->om_date)),
				);

				array_push($d, $data);
			}
			return $d;	
		}else{
			return false;
		}
	}

	public function getuOrderDetail(){
		$this->db->order_by('om_id', 'desc');
		$order = $this->db->get_where('order_mst',array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId']))->row();

		if(!empty($order)){
			$check_store = $this->db->get_where('store_mst',array('sm_id' => $order->om_store_id,))->row();

			if(!empty($check_store)){
				$store_details = array(
					'sm_id' => $check_store->sm_id,
					'sm_name' => $check_store->sm_name,
					'sm_email' => $check_store->sm_email,
					'sm_contact' => $check_store->sm_contact,
					'sm_address' => $check_store->sm_address,
					'sm_img' => $check_store->sm_img,
					'sm_latitude' => $check_store->sm_latitude,
					'sm_longitude' => $check_store->sm_longitude,
					'sm_status' => $check_store->sm_status,
					'sm_date' => date("d-m-Y", strtotime($check_store->sm_date)),	
				);
			}else{
				$store = json_decode($order->om_store_details);	
				$store_details = array(
					'sm_id' => $store->sm_id,
					'sm_name' => $store->sm_name,
					'sm_email' => $store->sm_email,
					'sm_contact' => $store->sm_contact,
					'sm_address' => $store->sm_address,
					'sm_img' => $store->sm_img,
					'sm_latitude' => $store->sm_latitude,
					'sm_longitude' => $store->sm_longitude,
					'sm_status' => $store->sm_status,
					'sm_date' => date("d-m-Y", strtotime($store->sm_date))
				);
			}

			$order_detail=array();
			foreach(json_decode($order->om_details) as $od){
				$check_review = $this->db->get_where('review_mst',array('rm_o_id'=>$order->om_id,'rm_p_id' => $od->pId,'rm_u_id' => $order->om_user_id,'rm_s_id' => $order->om_store_id));
				
				if($check_review->num_rows() > 0){
					$product = array(
						'pId' => $od->pId,
						'pName' => $od->pName,
						'pAmount' => $od->pAmount,
						'pQty' => $od->pQty,
						'pImage' => $od->pImage,
						'pReview' => '1',
					);
				}else{
					$product = array(
						'pId' => $od->pId,
						'pName' => $od->pName,
						'pAmount' => $od->pAmount,
						'pQty' => $od->pQty,
						'pImage' => $od->pImage,
						'pReview' => '0',
					);
				}
				array_push($order_detail,$product);
			}
			
			$d = array(
				'sm_id' => $store_details['sm_id'],
				'sm_name' => $store_details['sm_name'],
				'sm_email' => $store_details['sm_email'],
				'sm_contact' => $store_details['sm_contact'],
				'sm_address' => $store_details['sm_address'],
				'sm_img' => $store_details['sm_img'],
				'sm_latitude' => $store_details['sm_latitude'],
				'sm_longitude' => $store_details['sm_longitude'],
				'sm_status' => $store_details['sm_status'],
				'sm_date' => $store_details['sm_date'],
				'o_id' => $order->om_id,
				'o_u_id' => $order->om_user_id,
				'o_s_id' => $order->om_store_id,
				'order_id' => $order->om_order_id,
				'total_amount' => $order->om_total,
				'o_status' => $order->om_status,
				'o_date' => date("d-m-Y", strtotime($order->om_date)),
				'data' => $order_detail,
			);

			return $d;	
		}else{
			return false;
		}
	}

	public function getsOrderDetail(){
		$this->db->order_by('om_id', 'desc');
		$order = $this->db->get_where('order_mst',array('om_store_id' => $_GET['sId'],'om_id' => $_GET['oId']))->row();

		if(!empty($order)){
			$user = json_decode($order->om_user_details);

			$d = array(
				'um_id' => $user->um_id,
				'um_f_name' => $user->um_f_name,
				'um_l_name' => $user->um_l_name,
				'um_email' => $user->um_email,
				'um_contact' => $user->um_contact,
				'um_img' => $user->um_img,
				'um_date' => date("d-m-Y", strtotime($user->um_date)),
				'o_id' => $order->om_id,
				'o_u_id' => $order->om_user_id,
				'o_s_id' => $order->om_store_id,
				'order_id' => $order->om_order_id,
				'total_amount' => $order->om_total,
				'o_status' => $order->om_status,
				'o_date' => date("d-m-Y", strtotime($order->om_date)),
				'data' => json_decode($order->om_details),
			);

			return $d;	
		}else{
			return false;
		}
	}

	public function getScan(){
		$order = $this->db->get_where('order_mst',array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId'],'om_status'=>0))->row();

		if(!empty($order)){
			$order_update = $this->db->update('order_mst',array('om_status' => 1), array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId'],));

			$check = $this->db->get_where('order_mst',array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId'],'om_status'=>1));
			if($check->num_rows() > 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public function getuOrderCancel(){
		$order = $this->db->get_where('order_mst',array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId'],'om_status'=>0))->row();

		if(!empty($order)){

			$od = json_decode($order->om_details);

			foreach (json_decode($order->om_details) as $od) {
				$product_qty = $this->db->get_where('product_mst',array('pm_id' => $od->pId))->row();
				$qty_add = $product_qty->pm_qty + $od->pQty;
				$product_qty_update =$this->db->update('product_mst',array('pm_qty' => $qty_add),array('pm_id' => $od->pId));
			}

			$order_update = $this->db->update('order_mst',array('om_status' => 2), array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId'],));

			$check = $this->db->get_where('order_mst',array('om_user_id' => $_GET['uId'],'om_id' => $_GET['oId'],'om_status'=>2));
			if($check->num_rows() > 0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public function getReview(){
		$check_review = $this->db->get_where('review_mst',array('rm_o_id' => $_POST['oId'],'rm_p_id' => $_POST['pId'],'rm_s_id' => $_POST['sId'],'rm_u_id' => $_POST['uId']))->row();

		if(empty($check_review)){
			$data = array(
				'rm_o_id'=> $_POST['oId'],
				'rm_u_id'=> $_POST['uId'],
				'rm_s_id'=> $_POST['sId'],
				'rm_p_id'=> $_POST['pId'],
				'rm_q1'=> $_POST['qOne'],
				'rm_q2'=> $_POST['qTwo'],
				'rm_review'=> $_POST['review'],
				'rm_date'=> date('Y-m-d'),
			);

			$review_insert = $this->db->insert('review_mst',$data);
			$last_ri_id = $this->db->insert_id();

			if(!empty($last_ri_id)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public function getReviewShow(){
		$check_review = $this->db->get_where('review_mst',array('rm_o_id' => $_POST['oId'],'rm_p_id' => $_POST['pId'],'rm_s_id' => $_POST['sId'],'rm_u_id' => $_POST['uId']))->row();

		if(!empty($check_review)){
			$data = array(
				'rm_q1'=> $check_review->rm_q1,
				'rm_q2'=> $check_review->rm_q2,
				'rm_review'=> $check_review->rm_review,
				'rm_date'=> date("d-m-Y", strtotime($check_review->rm_date)),
			);
			return $data; 
		}else{
			return false;
		}
	}
}
