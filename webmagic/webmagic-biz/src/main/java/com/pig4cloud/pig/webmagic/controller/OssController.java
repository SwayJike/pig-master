package com.pig4cloud.pig.webmagic.controller;

import cn.hutool.core.map.MapBuilder;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.plugin.oss.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author SwayJike
 * @Date 2022/8/5
 * @ApiNote
 */
@RestController
@RequiredArgsConstructor
public class OssController {

	@Value("${oss.endpoint}")
	private String endpoint;

	private final OssTemplate template;

	private Random random = new Random();

	/**
	 * 上传文件
	 * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
	 *
	 * @param file 资源
	 * @return R(bucketName, filename)
	 */
	@SneakyThrows
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String ext = file.getOriginalFilename().split("\\.")[1];
		String name = UUID.randomUUID().toString();
		String filename = name + "." + ext;
		template.putObject(CommonConstants.BUCKET_NAME, filename, file.getInputStream());
		String url = template.getObjectURL(CommonConstants.BUCKET_NAME, filename);
		return R.ok(MapBuilder.create().put("url", url).build());
	}
}
