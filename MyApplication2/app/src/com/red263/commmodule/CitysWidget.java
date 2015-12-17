package com.red263.commmodule;

import com.red263.Edaijia.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class CitysWidget extends LinearLayout {
	private Spinner province;
	private Spinner city;
	private EditText area;

	private String cityval = "城市";

	public CitysWidget(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CitysWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.citys_widget, this);

		province = (Spinner) this.findViewById(R.id.province);
		city = (Spinner) this.findViewById(R.id.city);
		area = (EditText) findViewById(R.id.area);

		// (处理省的显示)
		// 将可选内容与ArrayAdapter的连接(从资源数组文件中获取数据)
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getContext(), R.array.province,
				android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// 将数据绑定到Spinner视图上
		province.setAdapter(adapter);

		// 添加条目被选中监听器
		province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// parent既是province对象
				Spinner spinner = (Spinner) parent;
				String pro = (String) spinner.getItemAtPosition(position);
				// (处理省的市的显示)
				// 将默认值与ArrayAdapter连接(从资源数组文件中获取数据)
				ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter
						.createFromResource(getContext(), R.array.c1,
								android.R.layout.simple_spinner_item);
				// 获取所在省含有哪些市(从资源数组文件中获取数据)

				if (pro.equals("北京市")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c1, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("天津市")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c2, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("河北省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c3, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("山西省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c4, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("内蒙古")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c5, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("辽宁省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c6, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("吉林省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c7, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("黑龙江省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c8, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("上海市")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c9, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("江苏省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c10, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("浙江省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c11, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("安徽省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c12, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("福建省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c13, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("江西省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c14, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("山东省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c15, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("河南省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c16, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("湖北省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c17, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("湖南省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c18, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("广东省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c19, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("广西自治区")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c20, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("海南省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c21, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("重庆市")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c22, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("四川省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c23, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("贵州省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c24, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("云南省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c25, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("西藏自治区")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c26, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("陕西省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c27, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("甘肃省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c28, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("青海省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c29, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("宁夏回族自治区")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c30, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("新疆维吾尔自治区")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c31, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("香港特别行政区")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c32, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("澳门特别行政区")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c33, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("台湾省")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c34, android.R.layout.simple_spinner_item);
				}
				if (pro.equals("其它")) {
					cityAdapter = ArrayAdapter.createFromResource(getContext(),
							R.array.c35, android.R.layout.simple_spinner_item);
				}
				// */
				// 绑定数据到Spinner(City)上
				city.setAdapter(cityAdapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/**
	 * 获取省份
	 */
	public String getProvinceVal() {
		String provinceval = province.getSelectedItem().toString();
		return provinceval;
	}

	/**
	 * 获取城市
	 */
	public String getCityVal() {
		cityval = city.getSelectedItem().toString();
		return cityval;
	}

	/**
	 * 获取县区
	 */
	public String getAreaVal() {
		String areaval = area.getText().toString();
		return areaval;
	}

	/**
	 * 设置县区
	 */
	public void setProvinceVal() {

	}

	/**
	 * 设置县区
	 */
	public void setCityVal() {

	}

	/**
	 * 设置县区
	 */
	public void setAreaVal(String areaval) {

		this.area.setText(areaval);
	}

}
