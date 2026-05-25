package com.search.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.search.platform.annotation.AuthCheck;
import com.search.platform.common.BaseResponse;
import com.search.platform.common.DeleteRequest;
import com.search.platform.common.ErrorCode;
import com.search.platform.common.ResultUtils;
import com.search.platform.constant.UserConstant;
import com.search.platform.exception.BusinessException;
import com.search.platform.exception.ThrowUtils;
import com.search.platform.model.dto.picture.PictureQueryRequest;
import com.search.platform.model.dto.post.PostAddRequest;
import com.search.platform.model.dto.post.PostEditRequest;
import com.search.platform.model.dto.post.PostQueryRequest;
import com.search.platform.model.dto.post.PostUpdateRequest;
import com.search.platform.model.entity.Picture;
import com.search.platform.model.entity.Post;
import com.search.platform.model.entity.User;
import com.search.platform.model.vo.PostVO;
import com.search.platform.service.PictureService;
import com.search.platform.service.PostService;
import com.search.platform.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片接口
 *
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                        HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }


}
